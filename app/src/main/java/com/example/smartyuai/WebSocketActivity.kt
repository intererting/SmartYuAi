package com.example.smartyuai

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.ByteString
import java.util.concurrent.TimeUnit

/**
 * @author    yiliyang
 * @date      2023/4/13 下午4:48
 * @version   1.0
 * @since     1.0
 */
class WebSocketActivity : AppCompatActivity() {

    var port: Int = 0
    lateinit var webSocket: WebSocket
    lateinit var serverWebSocket: WebSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_websocket)
        Thread {
            startMockService()
        }.start()

        findViewById<Button>(R.id.send).setOnClickListener {
            webSocket.send("hello www")
        }

        findViewById<Button>(R.id.start).setOnClickListener {
            val okHttpClient = OkHttpClient.Builder()
                .pingInterval(50, TimeUnit.SECONDS)
                .build()
            val wsUrl = "ws://localhost:$port"
            val request = Request.Builder().url(wsUrl).build()
            println("main ${Thread.currentThread()}")
            okHttpClient.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    this@WebSocketActivity.webSocket = webSocket
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    println("main message ${Thread.currentThread()}")
                    webSocket.send("hello from clint")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                }
            })
        }
    }

    private fun startMockService() {
        val mockService = MockWebServer()
        println("mock ${Thread.currentThread()}")
        mockService.enqueue(MockResponse().withWebSocketUpgrade(object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                serverWebSocket = webSocket
                super.onOpen(webSocket, response)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                serverWebSocket.send("reply")
                super.onMessage(webSocket, text)
            }
        }))
//        mockService.start(80)
//        mockService.enqueue(MockResponse().apply {
//            setBody("result from server")
//        })
        println(mockService.port)
        println(mockService.hostName)
        port = mockService.port
    }

}