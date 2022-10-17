package com.demo.orangeapplock.local

import com.demo.orangeapplock.bean.ServerInfoBean

object LocalManager {

    const val appLockLocalRao="""{
    "s":1,
    "l":[
    
    ]
}"""

    val appLockLocalServerList= arrayListOf(
        ServerInfoBean(
            host = "100.223.52.0",
            pwd = "123456",
            country = "Japan",
            city = "Tokyo",
            port = 100,
            method = "chacha20-ietf-poly1305"
        ),
        ServerInfoBean(
            host = "100.223.52.78",
            pwd = "123456",
            country = "UnitedStates",
            city = "Tokyo",
            port = 100,
            method = "chacha20-ietf-poly1305"
        )
    )

    const val appLockLocalAd="""{
    "click":15,
    "show":50,
    "open": [
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/3419835294",
            "type": "cp",
            "sort": 1
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/3419835294A",
            "type": "kp",
            "sort": 2
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/3419835294AA",
            "type": "kp",
            "sort": 3
        }
    ],
    "home": [
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110",
            "type": "ys",
            "sort": 2
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110AAA",
            "type": "ys",
            "sort": 1
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110AA",
            "type": "ys",
            "sort": 3
        }
    ],
    "result": [
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110A",
            "type": "ys",
            "sort": 2
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110",
            "type": "ys",
            "sort": 1
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110AA",
            "type": "ys",
            "sort": 3
        }
    ],
    "connect": [
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/8691691433A",
            "type": "cp",
            "sort": 2
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/1033173712",
            "type": "cp",
            "sort": 1
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/1033173712AA",
            "type": "cp",
            "sort": 3
        }
    ],
    "back": [
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/1033173712A",
            "type": "cp",
            "sort": 2
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/8691691433",
            "type": "cp",
            "sort": 1
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/1033173712AA",
            "type": "cp",
            "sort": 3
        }
    ]
}"""
}