package com.orangeapplock.pro.local

import com.orangeapplock.pro.bean.ServerInfoBean

object LocalManager {
    const val appLockEmail="rubengjkerjw@gmail.com"
    const val appLockPolicy="https://sites.google.com/view/orangeapplock/%E9%A6%96%E9%A1%B5"

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
    "click": 15,
    "show": 50,
    "open": [
        {
            "source": "admob",
            "id": "ca-app-pub-1045189412343736/1753755821",
            "type": "kai",
            "sort": 1
        }
    ],
    "home": [
        {
            "source": "admob",
            "id": "ca-app-pub-1045189412343736/4775504980",
            "type": "yuan",
            "sort": 1
        }
    ],
    "app_lock_home": [
        {
            "source": "admob",
            "id": "ca-app-pub-1045189412343736/2149341645",
            "type": "yuan",
            "sort": 1
        }
    ],
    "lock": [
        {
            "source": "admob",
            "id": "ca-app-pub-1045189412343736/4392361608",
            "type": "cha",
            "sort": 1
        }
    ]
}"""
}