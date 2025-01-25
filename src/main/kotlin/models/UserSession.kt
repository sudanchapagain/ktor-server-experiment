package np.com.sudanchapagain.models

import kotlinx.serialization.*

@Serializable
data class UserSession(val id: String, val username: String)