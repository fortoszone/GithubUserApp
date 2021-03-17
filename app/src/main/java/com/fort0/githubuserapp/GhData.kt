package com.fort0.githubuserapp

object GhData {
    private val fname = arrayOf("Devwanto Dariel Nugroho")

    private val uname = arrayOf("fortoszone")

    private val userpic = intArrayOf(R.drawable.deva)

    val listData: ArrayList<GhUser>
        get() {
            val list = arrayListOf<GhUser>()
            for (position in fname.indices) {
                val gh = GhUser()
                gh.uname = uname[position]
                gh.userpic = userpic[position]
                list.add(gh)
            }
            return list
        }
}