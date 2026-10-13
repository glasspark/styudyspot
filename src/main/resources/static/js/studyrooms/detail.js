$(function () {

    const roomId = $('#detail').data('room-id');
    const roomMessage = $('#roomMessage');
    const reserveMessage = $('#reserveMessage');
    const reserveButton = $('#reserveButton');

    $('#date').attr('min', Common.today()).val(Common.today());

    $.ajax({
        url: '/api/studyroom/' + roomId,
        type: 'GET',
        dataType: 'json',
        success: function (res) {
            showRoom(res.RESULT);
        },
        error: function (xhr, textStatus) {
            Common.handleError(xhr, textStatus, roomMessage);
        }
    });

    function showRoom(room) {
        $('#roomName').text(room.name);
        $('#roomAddress').text(room.address);
        $('#roomCapacity').text(room.capacity + '명');
        $('#roomHours').text(Common.formatOperatingHours(room.openTime, room.closeTime));
        $('#roomStatus').text(room.statusDescription);
        $('#roomInfo').prop('hidden', false);

        if (room.status === 'ACTIVE') {
            $('#reserveSection').prop('hidden', false);
        } else {
            roomMessage.text('현재 예약할 수 없는 스터디룸입니다. (' + room.statusDescription + ')').addClass('error');
        }
    }

    $('#reserveForm').on('submit', function (e) {
        e.preventDefault();

        const date = $('#date').val();
        const start = $('#startTime').val();
        const end = $('#endTime').val();
        const endDate = end <= start ? Common.addDays(date, 1) : date;

        reserveMessage.text('').removeClass('error success');
        reserveButton.prop('disabled', true);   // 연속 클릭으로 두 번 예약되지 않게

        $.ajax({
            url: '/api/reservations',
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({
                studyRoomId: roomId,
                reserverName: $('#reserverName').val().trim(),
                startAt: date + 'T' + start + ':00',
                endAt: endDate + 'T' + end + ':00'
            }),
            success: function () {
                alert('예약이 완료되었습니다.');
                location.href = '/reservations';
            },
            error: function (xhr, textStatus) {
                Common.handleError(xhr, textStatus, reserveMessage);
            },
            complete: function () {
                reserveButton.prop('disabled', false);
            }
        });
    });
});
