var publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWlALa1Kem5BwEDTGPV7T++lp1IlJBuVQlc4fLzgOXpPUMdty6sfaRymdJAyL7exRUo6VFkc5NWdFw/OPiL1GHDVhF2NXAZrNAJUN9/tnn30rQC/e+xVMMYwelMRPLp+HPx/yI1gx9Wcj0iHMXfpks6ymynQL5GWc9A1jJgAvYJwIDAQAB";
var privateKey = "QKBgQCWlA";
var encrypt = new JSEncrypt();
encrypt.setPublicKey(publicKey);

//传入需要加密的json对象，返回加密后的json内容
function myEncrypt(jsonObj) {
    var jsonData = {};
    jsonData["requestData"] = encrypt.encrypt(JSON.stringify(jsonObj));
    // echo("加密后内容：")
    // echo(encrypt.encrypt(JSON.stringify(jsonObj)))
    return JSON.stringify(jsonData)
}

function echo(res) {
    console.log(res)
}

function echoObject(object){
    console.log(JSON.stringify(object))
}

//初始化语言配置
function initLaunage() {
    //初始化i18n插件
    var settings = {
        path: ctx + 'i18n/',//这里表示访问路径
        name: 'message',//文件名开头
        language: LANG_COUNTRY,//文件名语言 例如en_US
        mode: 'both'//默认值
    };
    $.i18n.properties(settings);
}

//初始化i18n函数
function i18n(msgKey) {
    try {
        return $.i18n.prop(msgKey);
    } catch (e) {
        return msgKey;
    }
}

/**
 * 创建签名
 * @param params
 * @returns {string}
 */
function createSign (params) {
    var paramStr = "";
    if (typeof params == "string") {
        params += "&privateKey="+privateKey;
        paramStr = params;
    }
    else if (typeof params == "object") {
        var arr = [];
        params["privateKey"] = privateKey;
        for (var i in params) {
            if (params.hasOwnProperty(i)) {
                arr.push((i + "=" + params[i]));
            }
        }
        paramStr = arr.join(("&"));
    }
    if (paramStr) {
        var newParamStr = paramStr.split("&").sort().join("&");
        console.log(newParamStr);
        var sign = md5(newParamStr);
        /*if (typeof params == "string") {
            params += ("&sign=" + sign);
        }
        else {
            params["sign"] = sign;
        }*/
    }
    return sign.toUpperCase();;
}


