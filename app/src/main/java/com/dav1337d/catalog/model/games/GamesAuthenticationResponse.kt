package com.dav1337d.catalog.model.games

data class GamesAuthenticationResponse constructor(val access_token: String, val expires_in: Int, val token_type: String)
