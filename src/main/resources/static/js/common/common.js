// 여러 화면에서 같이 쓰는 함수 모음. (jQuery 가 먼저 로드되어 있어야 한다)
const Common = {

    // 서버 JSON 의 MESSAGE 를 꺼낸다. JSON 이 아니면 기본 문구.
    errorMessage: function (xhr) {
        if (xhr && xhr.responseJSON && xhr.responseJSON.MESSAGE) {
            return xhr.responseJSON.MESSAGE;
        }
        return '요청 처리 중 오류가 발생했습니다.';
    },

    // $.ajax 의 error 콜백에서 공통으로 쓴다.
    // 로그인이 풀리면 /api/** 가 로그인 페이지(HTML)로 리다이렉트되어 JSON 파싱이 실패하므로 로그인 화면으로 보낸다.
    handleError: function (xhr, textStatus, messageBox) {
        if (textStatus === 'parsererror') {
            location.href = '/members/login';
            return;
        }
        if (messageBox) {
            messageBox.text(Common.errorMessage(xhr)).removeClass('success').addClass('error');
        }
    },

    // "2030-01-01T10:00:00" -> "2030-01-01 10:00"
    formatDateTime: function (iso) {
        return iso ? iso.substring(0, 16).replace('T', ' ') : '';
    },

    // "09:00:00" -> "09:00"
    formatTime: function (time) {
        return time ? time.substring(0, 5) : '';
    },

    // 운영 시간 표시. 24시간(시작 = 종료)과 야간(종료가 시작보다 이름)을 풀어서 보여준다.
    formatOperatingHours: function (open, close) {
        const o = Common.formatTime(open), c = Common.formatTime(close);
        if (o === c) return '24시간 운영';
        if (c < o) return o + ' ~ 다음 날 ' + c;
        return o + ' ~ ' + c;
    },

    // 페이지 버튼을 그린다. pageInfo 는 서버의 PageResponse.page 그대로.
    // 서버 값은 .text() 로만 넣는다. (HTML 로 합치면 XSS 위험)
    renderPagination: function (container, pageInfo, onClick) {
        container.empty();
        if (!pageInfo || pageInfo.totalPages <= 1) return;

        const current = pageInfo.current;
        const first = Math.max(0, current - 2);
        const last = Math.min(pageInfo.totalPages - 1, current + 2);

        const add = function (label, page, disabled) {
            const btn = $('<button type="button"></button>').text(label).prop('disabled', disabled);
            if (!disabled) btn.on('click', function () { onClick(page); });
            container.append(btn);
        };

        add('이전', current - 1, pageInfo.first);
        for (let p = first; p <= last; p++) {
            add(String(p + 1), p, p === current);
        }
        add('다음', current + 1, pageInfo.last);
    },

    // 오늘 날짜를 로컬 시간 기준 YYYY-MM-DD 로.
    today: function () {
        const d = new Date();
        return Common.toDateString(d);
    },

    toDateString: function (d) {
        const pad = function (n) { return String(n).padStart(2, '0'); };
        return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate());
    },

    // "YYYY-MM-DD" 에 days 일을 더한다. (Date 의 시간대 변환을 피하려고 연, 월, 일을 직접 계산)
    addDays: function (dateString, days) {
        const parts = dateString.split('-').map(Number);
        return Common.toDateString(new Date(parts[0], parts[1] - 1, parts[2] + days));
    }
};
