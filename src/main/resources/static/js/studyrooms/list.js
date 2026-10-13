$(function () {

    const SIZE = 10;
    const body = $('#roomList');
    const message = $('#message');

    function load(page) {
        const params = {page: page, size: SIZE};
        const keyword = $('#keyword').val().trim();
        const minCapacity = $('#minCapacity').val();
        if (keyword) params.keyword = keyword;          // 비어 있는 조건은 아예 보내지 않는다
        if (minCapacity) params.minCapacity = minCapacity;

        message.text('').removeClass('error');

        $.ajax({
            url: '/api/studyroom',
            type: 'GET',
            data: params,
            dataType: 'json',
            success: function (res) {
                render(res.RESULT);
            },
            error: function (xhr, textStatus) {
                Common.handleError(xhr, textStatus, message);
            }
        });
    }

    // 서버에서 온 값은 .text() 로만 넣는다.
    function render(result) {
        body.empty();

        if (result.list.length === 0) {
            body.append($('<tr></tr>').append($('<td colspan="4" class="empty"></td>').text('조건에 맞는 스터디룸이 없습니다.')));
        }

        result.list.forEach(function (room) {
            const link = $('<a></a>').attr('href', '/studyrooms/' + room.studyRoomId).text(room.name);
            body.append($('<tr></tr>')
                .append($('<td></td>').append(link))
                .append($('<td></td>').text(room.address))
                .append($('<td></td>').text(room.capacity + '명'))
                .append($('<td></td>').text(Common.formatOperatingHours(room.openTime, room.closeTime))));
        });

        Common.renderPagination($('#pagination'), result.page, load);
    }

    $('#searchForm').on('submit', function (e) {
        e.preventDefault();
        load(0);
    });

    load(0);
});
