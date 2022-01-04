var socket
var heartCheck

function openSocket (userId, websocketServiceName, febs) {

  // console.log("openSocket userId = " + userId + " websocketServiceName = " + websocketServiceName)
  if (typeof (WebSocket) == 'undefined') {
    alert('您的浏览器不支持WebSocket')
    console.log('您的浏览器不支持WebSocket')
  } else {

    // 心跳检测, 每隔一段时间检测连接状态，如果处于连接中，就向server端主动发送消息，来重置server端与客户端的最大连接时间，如果已经断开了，发起重连。
    heartCheck = {
      timeout: 55000,        // 30s发一次心跳，比server端设置的连接时间稍微小一点，在接近断开的情况下以通信的方式去重置连接时间。
      serverTimeoutObj: null,
      reset: function () {
        clearTimeout(this.serverTimeoutObj)
        return this
      },
      start: function () {
        var self = this
        this.serverTimeoutObj = setInterval(function () {
          if (socket.readyState == 1) {
            // console.log("连接状态，发送消息保持连接");
            socket.send('ping')
            heartCheck.reset().start()    // 如果获取到消息，说明连接是正常的，重置心跳检测
          } else {
            console.log('断开状态，尝试重连')
            heartCheck.reset()
            openSocket(userId, websocketServiceName, febs)
          }
        }, this.timeout)
      }
    }
    // console.log("您的浏览器支持WebSocket");
    //实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
    var socketUrl = 'http://' + window.location.host + ctx + websocketServiceName + '/' + userId
    var replaceStr = 'ws'
    //需要https的时候才能使用这个
    // replaceStr = "wss";
    socketUrl = socketUrl.replace('https', replaceStr).replace('http', replaceStr)

    // console.log("连接地址为：" + socketUrl);
    if (socket != null) {
      socket.close()
      socket = null
    }
    socket = new WebSocket(socketUrl)
    //打开事件
    socket.onopen = function () {
      // console.log("websocket已打开");
      heartCheck.reset().start()   // 成功建立连接后，重置心跳检测
      //socket.send("这是来自客户端的消息" + location.href + new Date());
    }
    //获得消息事件
    socket.onmessage = function (msg) {
      // console.log("获得消息事件")
      // console.log(msg)
      heartCheck.reset().start()    // 如果获取到消息，说明连接是正常的，重置心跳检测
      var type = JSON.parse(msg.data).type
      var data = JSON.parse(msg.data).data
      if(data != "ping"){
        console.log('web收到消息：' + data)
      }
      switch (type) {
        case 1:
          //1 发送成功
          // console.log("发送成功")
          break
        case 2:
          //2 全部阅读
          // console.log("全部阅读了消息")
          document.getElementById('hotDot').style.display = 'none'
          break
        case 3:
          //3 付款超时报警
          // console.log("付款超时报警")
          document.getElementById('hotDot').style.display = 'inline-block'
          break
        case 4:
          //4 闭环超时报警
          // console.log("闭环超时报警")
          document.getElementById('hotDot').style.display = 'inline-block'
          break
        case 5:
          //点击打印结果
          console.log('点击打印结果')

          splits = data.split('-')
          eventType = splits[0]
          printJobId = splits[1]
          console.log(printJobId)
          if (eventType === 'receive') {
            let res = splits[2]
            console.log(res)

            var msg = '1'.equals(res) ? '操作成功' : '操作失败'
            febs.alert.success(msg)

          } else if (eventType === 'overtime') {
              febs.alert.warn('机器返回结果超时')
          }
          $('#febs-printJob').find('#refreshQuery').click()
          // document.getElementById("hotDot").style.display = "inline-block";
          break
        case 6:
          //点击取消打印结果
          console.log('点击取消打印结果')

          splits = data.split('-')
          eventType = splits[0]
          printJobId = splits[1]
          console.log(printJobId)
          if (eventType === 'receive') {
            let res = splits[2]
            console.log(res)

            var msg = '1'.equals(res) ? '操作成功' : '操作失败'
            febs.alert.success('点击取消打印结果' + msg)

          } else if (eventType === 'overtime') {
            febs.alert.warn('机器返回结果超时')
          }
          $('#febs-printJob').find('#refreshQuery').click()
          // document.getElementById("hotDot").style.display = "inline-block";
          break
        case 7:
          $('#febs-printJob').find('#refreshQuery').click()
          break
        case 8:
            febs.alert.success(msg)
          $('#febs-printJob').find('#refreshQuery').click()
          location.reload()
          break
      }
    }
    //关闭事件
    socket.onclose = function (e) {
      console.log('websocket已关闭 onclose' + e.code + ' ' + e.reason + ' ' + e.wasClean)
    }
    //发生了错误事件
    socket.onerror = function (res) {
      console.log(JSON.stringify(res))
      console.log('websocket发生了错误')
    }

    // 监听窗口事件，当窗口关闭时，主动断开websocket连接，防止连接没断开就关闭窗口，server端报错
    window.onbeforeunload = function () {
      console.log('websocket已关闭 onbeforeunload')
      socket.close()
    }
  }
}

/**
 * 发送消息给服务器
 * @param msg
 */
function sendMessage (msg) {
  if (typeof (WebSocket) == 'undefined') {
    console.log('您的浏览器不支持WebSocket')
  } else {
    if (socket == null) {
      alert('请先开启socket')
      return
    }
    console.log('您的浏览器支持WebSocket,正在发送数据')
    socket.send(msg)
  }
}