package com.demo.orangeapplock.local

import com.demo.orangeapplock.bean.ServerInfoBean

object LocalManager {
    const val appLockEmail=""
    const val appLockPolicy=""

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
            "type": "cha",
            "sort": 1
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/3419835294A",
            "type": "kai",
            "sort": 2
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/3419835294AA",
            "type": "kai",
            "sort": 3
        }
    ],
    "home": [
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110",
            "type": "yuan",
            "sort": 2
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110AAA",
            "type": "yuan",
            "sort": 1
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110AA",
            "type": "yuan",
            "sort": 3
        }
    ],
    "app_lock_home": [
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110",
            "type": "yuan",
            "sort": 2
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110AAA",
            "type": "yuan",
            "sort": 1
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110AA",
            "type": "yuan",
            "sort": 3
        }
    ],
      "lock": [
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/8691691433A",
            "type": "cha",
            "sort": 2
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/1033173712",
            "type": "cha",
            "sort": 1
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/1033173712AA",
            "type": "cha",
            "sort": 3
        }
    ],
    "result": [
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110A",
            "type": "yuan",
            "sort": 2
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110",
            "type": "yuan",
            "sort": 1
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/2247696110AA",
            "type": "yuan",
            "sort": 3
        }
    ],
    "connect": [
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/8691691433A",
            "type": "cha",
            "sort": 2
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/1033173712",
            "type": "cha",
            "sort": 1
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/1033173712AA",
            "type": "cha",
            "sort": 3
        }
    ],
    "back": [
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/1033173712A",
            "type": "cha",
            "sort": 2
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/8691691433",
            "type": "cha",
            "sort": 1
        },
        {
            "source": "admob",
            "id": "ca-app-pub-3940256099942544/1033173712AA",
            "type": "cha",
            "sort": 3
        }
    ]
}"""
}