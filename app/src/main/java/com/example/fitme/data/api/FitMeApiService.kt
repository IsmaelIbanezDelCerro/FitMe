package com.example.fitme.data.api

import retrofit2.http.*

interface FitMeApiService {

    // --- Usuario ---
    @POST("usuarios/registro")
    suspend fun registrar(@Body usuario: UsuarioDto): UsuarioDto

    @POST("usuarios/login")
    suspend fun login(@Body request: LoginRequest): UsuarioDto

    @GET("usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: Int): UsuarioDto

    @PUT("usuarios/{id}")
    suspend fun updateUsuario(@Path("id") id: Int, @Body usuario: UsuarioDto): UsuarioDto

    // --- Registro de peso ---
    @GET("usuarios/{usuarioId}/pesos")
    suspend fun getPesos(@Path("usuarioId") usuarioId: Int): List<RegistroPesoDto>

    @POST("usuarios/{usuarioId}/pesos")
    suspend fun addPeso(@Path("usuarioId") usuarioId: Int, @Body peso: RegistroPesoDto): RegistroPesoDto

    @GET("usuarios/{usuarioId}/pesos/ultimo")
    suspend fun getUltimoPeso(@Path("usuarioId") usuarioId: Int): RegistroPesoDto

    // --- Rutinas ---
    @GET("usuarios/{usuarioId}/rutinas")
    suspend fun getRutinas(@Path("usuarioId") usuarioId: Int): List<RutinaDto>

    @POST("usuarios/{usuarioId}/rutinas")
    suspend fun addRutina(@Path("usuarioId") usuarioId: Int, @Body rutina: RutinaDto): RutinaDto

    @DELETE("rutinas/{id}")
    suspend fun deleteRutina(@Path("id") id: Int)

    // --- Ejercicios ---
    @GET("rutinas/{rutinaId}/ejercicios")
    suspend fun getEjercicios(@Path("rutinaId") rutinaId: Int): List<EjercicioDto>

    @POST("rutinas/{rutinaId}/ejercicios")
    suspend fun addEjercicio(@Path("rutinaId") rutinaId: Int, @Body ejercicio: EjercicioDto): EjercicioDto

    @PUT("ejercicios/{id}")
    suspend fun updateEjercicio(@Path("id") id: Int, @Body ejercicio: EjercicioDto): EjercicioDto

    @DELETE("ejercicios/{id}")
    suspend fun deleteEjercicio(@Path("id") id: Int)

    // --- Menú semanal ---
    @GET("usuarios/{usuarioId}/menus")
    suspend fun getMenus(@Path("usuarioId") usuarioId: Int): List<MenuSemanalDto>

    @POST("usuarios/{usuarioId}/menus")
    suspend fun addMenu(@Path("usuarioId") usuarioId: Int, @Body menu: MenuSemanalDto): MenuSemanalDto

    @DELETE("menus/{id}")
    suspend fun deleteMenu(@Path("id") id: Int)

    @GET("menus/{menuId}/dias")
    suspend fun getDiasMenu(@Path("menuId") menuId: Int): List<DiaMenuDto>

    @POST("menus/{menuId}/dias")
    suspend fun addDiaMenu(@Path("menuId") menuId: Int, @Body dia: DiaMenuDto): DiaMenuDto

    @PUT("dias/{id}")
    suspend fun updateDiaMenu(@Path("id") id: Int, @Body dia: DiaMenuDto): DiaMenuDto

    @DELETE("dias/{id}")
    suspend fun deleteDiaMenu(@Path("id") id: Int)

    // --- Racha ---
    @GET("usuarios/{usuarioId}/racha")
    suspend fun getRacha(@Path("usuarioId") usuarioId: Int): RachaDto

    @POST("usuarios/{usuarioId}/racha/check")
    suspend fun registrarCheck(@Path("usuarioId") usuarioId: Int): RachaDto

    // --- Preferencias alimenticias ---
    @GET("usuarios/{usuarioId}/preferencias")
    suspend fun getPreferencias(@Path("usuarioId") usuarioId: Int): List<PreferenciaDto>

    @POST("usuarios/{usuarioId}/preferencias")
    suspend fun addPreferencia(@Path("usuarioId") usuarioId: Int, @Body pref: PreferenciaDto): PreferenciaDto

    @DELETE("preferencias/{id}")
    suspend fun deletePreferencia(@Path("id") id: Int)

    // --- Gimnasios ---
    @GET("usuarios/{usuarioId}/gimnasios")
    suspend fun getGimnasios(@Path("usuarioId") usuarioId: Int): List<GimnasioDto>

    @POST("usuarios/{usuarioId}/gimnasios")
    suspend fun addGimnasio(@Path("usuarioId") usuarioId: Int, @Body gimnasio: GimnasioDto): GimnasioDto

    @DELETE("gimnasios/{id}")
    suspend fun deleteGimnasio(@Path("id") id: Int)
}
