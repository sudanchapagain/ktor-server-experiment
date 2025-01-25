package np.com.sudanchapagain.models

object BalanceModel {
    private val balances = mutableMapOf<String, Int>()

    fun getBalance(username: String): Int {
        return balances[username] ?: 0
    }

    fun updateBalance(username: String, amount: Int) {
        val currentBalance = balances.getOrDefault(username, 0)
        balances[username] = currentBalance + amount
    }
}
