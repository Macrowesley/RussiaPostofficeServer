// 定义常用的校验，常用的正则 https://www.open-open.com/code/view/1430625516632
layui.define(['jquery'], function (exports) {
    var $ = layui.jquery;
    exports('validate', {
        username: function (value, item) {
            if (!isEmpty(value)) {
                var result = '';
                $.ajax({
                    url: ctx + 'user/check/' + value,
                    data: {
                        "userId": item.getAttribute('id')
                    },
                    async: false,
                    type: 'get',
                    success: function (d) {
                        (!d) && (result = i18n("validation.userExists"))
                    }
                });
                if (!isEmpty(result)) {
                    return result;
                }
            }
        },
        cron: function (value, item) {
            if (!isEmpty(value)) {
                var result = '';
                $.ajax({
                    url: ctx + 'job/cron/check',
                    data: {
                        "cron": value
                    },
                    async: false,
                    type: 'get',
                    success: function (d) {
                        (!d) && (result = i18n("validation.cronNotValid"))
                    }
                });
                if (!isEmpty(result)) {
                    return result;
                }
            }
        },
        email: function (value) {
            if (!isEmpty(value)) {
                if (!new RegExp("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$").test(value)) {
                    return i18n("validation.email");
                }
            }
        },
        phone: function (value) {
            if (!isEmpty(value)) {
                if (!new RegExp("^1\\d{10}$").test(value)) {
                    return i18n("validation.phone");
                }
            }
        },
        number: function (value) {
            if (!isEmpty(value)) {
                if (!new RegExp("^[0-9]*$").test(value)) {
                    return i18n("validation.number");
                }
            }
        },
        range: function (value, item) {
            var minlength = item.getAttribute('minlength') ? item.getAttribute('minlength') : -1;
            var maxlength = item.getAttribute('maxlength') ? item.getAttribute('maxlength') : -1;
            var length = value.length;
            if (minlength === -1) {
                if (length > maxlength) {
                    return i18n("validation.lenNotMoreThen") + maxlength + i18n("validation.char");
                }
            } else if (maxlength === -1) {
                if (length < minlength) {
                    return i18n("validation.lenNotLessThen") + minlength + i18n("validation.char");
                }
            } else {
                if (length > maxlength || length < minlength) {
                    return i18n("validation.lenBetween") + minlength + ' ~ ' + maxlength + i18n("validation.char");
                }
            }
        }
    });

    function isEmpty(obj) {
        return typeof obj == 'undefined' || obj == null || obj === '';
    }
});