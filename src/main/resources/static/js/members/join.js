$(function () {

    $('#joinForm').on('submit', function (e) {
        e.preventDefault();

        $('#joinError').prop('hidden', true);

        $.ajax({
            url: '/api/members',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                username: $('#username').val(),
                email: $('#email').val(),
                password: $('#password').val(),
                nickname: $('#nickname').val()
            }),
            success: function (res) {
                location.href = '/members/login?joined=true';
            },
            error: function (xhr) {
                const msg = xhr.responseJSON ? xhr.responseJSON.MESSAGE : '요청에 실패했습니다.';
                $('#joinError').text(msg).prop('hidden', false);
            }
        });
    });

});