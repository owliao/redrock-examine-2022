package cn.udday.utils

import USER_ID
import cn.udday.tables.UserDao
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant
import java.util.*

object Auth {
	private const val SECRET_KEY = "REDROCKREDROCKREDROCKREDROCKREDROCKREDRO"
	private val algorithm = Algorithm.HMAC512(SECRET_KEY)

	private const val REFRESH_SECRET_KEY = "REDROCKREDROCKREDROCKREDROCKREDROCKREDRO"
	private val refreshAlgorithm = Algorithm.HMAC512(REFRESH_SECRET_KEY)
	private const val validityInMs = 1000L * 60 * 60 * 2
	private const val refreshValidityInMs = 1000L * 60 * 60 * 24 * 30
	private const val ISSUER = "REDROCK"

	fun makeJwtVerifier(isRefreshToken: Boolean = false): JWTVerifier = JWT
		.require(if (isRefreshToken) refreshAlgorithm else algorithm)
		.withIssuer(ISSUER)
		.build()

	fun makeToken(userID: Long, isRefreshToken: Boolean = false): String = JWT.create()
		.withSubject("Authentication")
		.withIssuer(ISSUER)
		.withIssuedAt(Date())
		.withClaim(USER_ID, userID)
		.withExpiresAt(getExpiration(isRefreshToken))
		.sign(if (isRefreshToken) refreshAlgorithm else algorithm)

	fun makeLongToken(userID: Long) = JWT.create()
		.withSubject("Authentication")
		.withIssuer(ISSUER)
		.withIssuedAt(Date())
		.withClaim(USER_ID, userID)
		.sign(algorithm)

	private fun getExpiration(isRefreshToken: Boolean) =
		Date(Instant.now().toEpochMilli() + if (isRefreshToken) refreshValidityInMs else validityInMs)


	fun getUserID(call: ApplicationCall): Long? {
		val principal = call.principal<JWTPrincipal>()
		return principal?.payload?.getClaim(USER_ID)?.asLong()
	}
}

fun ApplicationCall.getUserID(): Long {
	return Auth.getUserID(this)!!
}


suspend fun ApplicationCall.getUser(): UserDao? {
	return newSuspendedTransaction {
		UserDao.findById(Auth.getUserID(this@getUser)!!)
	}
}


fun ApplicationCall.headerToken(userID: Long) {
	val token = Auth.makeToken(userID)
	val refreshToken = Auth.makeToken(userID, true)
	this.response.header("token", token)
	this.response.header("refreshToken", refreshToken)
}