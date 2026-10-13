$(function () {

    const SIZE = 10;
    const body = $('#reservationList');
    const message = $('#message');
    let currentPage = 0;

    function load(page) {
        currentPage = page;
        $.ajax({
            url: '/api/reservations/me',
            type: 'GET',
            data: {page: page, size: SIZE},
            dataType: 'json',
            success: function (res) {
                render(res.RESULT);
            },
            error: function (xhr, textStatus) {
                Common.handleError(xhr, textStatus, message);
            }
        });
    }

    // 서버에서 온 값은 .text() 로만 넣는다. (예약자 이름은 사용자가 입력한 값이라 특히 중요)
    function render(result) {
        body.empty();

        if (result.list.length === 0) {
            body.append($('<tr></tr>').append($('<td colspan="6" class="empty"></td>').text('예약 내역이 없습니다.')));
        }

        result.list.forEach(function (reservation) {
            const room = $('<a></a>').attr('href', '/studyrooms/' + reservation.studyRoomId).text(reservation.studyRoomName);
            const status = $('<span class="badge"></span>').text(reservation.statusDescription);
            if (reservation.status === 'CANCELLED') status.addClass('cancelled');

            const action = $('<td></td>');
            if (reservation.status === 'CONFIRMED') {
                action.append($('<button type="button"></button>').text('취소').on('click', function () {
                    cancel(reservation.reservationId);
                }));
            }

            body.append($('<tr></tr>')
                .append($('<td></td>').append(room))
                .append($('<td></td>').text(reservation.reserverName))
                .append($('<td></td>').text(Common.formatDateTime(reservation.startAt)))
                .append($('<td></td>').text(Common.formatDateTime(reservation.endAt)))
                .append($('<td></td>').append(status))
                .append(action));
        });

        Common.renderPagination($('#pagination'), result.page, load);
    }

    function cancel(reservationId) {
        if (!confirm('이 예약을 취소하시겠습니까?')) return;

        message.text('').removeClass('error success');
        $.ajax({
            url: '/api/reservations/' + reservationId + '/cancel',
            type: 'PATCH',
            dataType: 'json',
            success: function () {
                message.text('예약이 취소되었습니다.').addClass('success');
                load(currentPage);
            },
            error: function (xhr, textStatus) {
                Common.handleError(xhr, textStatus, message);
            }
        });
    }

    load(0);
});
