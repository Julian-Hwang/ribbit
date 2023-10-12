package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.AuthApiService
import com.hippoddung.ribbit.network.apiRequestFlow
import com.hippoddung.ribbit.network.bodys.Auth
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService
) {
    fun login(auth: Auth) = apiRequestFlow {
        authApiService.login(auth)
    }
}