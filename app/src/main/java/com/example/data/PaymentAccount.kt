package com.example.data

enum class BankProvider {
  TELEBIRR,
  CBE,
  ABYSSINIA,
}

data class PaymentAccount(
  val id: String,
  val displayName: String,
  val methodOption: String,
  val accountNumber: String,
  val ownerName: String,
  val provider: BankProvider,
)

object PaymentAccountsCatalog {
  const val RECIPIENT_OWNER = "አዲሴ አራጋው"

  val accounts: List<PaymentAccount> =
    listOf(
      PaymentAccount(
        id = "telebirr_1",
        displayName = "ቴሌብር (Telebirr)",
        methodOption = "ቴሌብር (0942320888)",
        accountNumber = "0942320888",
        ownerName = RECIPIENT_OWNER,
        provider = BankProvider.TELEBIRR,
      ),
      PaymentAccount(
        id = "telebirr_2",
        displayName = "ቴሌብር (Telebirr 2)",
        methodOption = "ቴሌብር (0925792262)",
        accountNumber = "0925792262",
        ownerName = RECIPIENT_OWNER,
        provider = BankProvider.TELEBIRR,
      ),
      PaymentAccount(
        id = "cbe",
        displayName = "የኢትዮጵያ ንግድ ባንክ (CBE)",
        methodOption = "ንግድ ባንክ (CBE)",
        accountNumber = "1000282540634",
        ownerName = RECIPIENT_OWNER,
        provider = BankProvider.CBE,
      ),
      PaymentAccount(
        id = "abyssinia",
        displayName = "አቢሲኒያ ባንክ (Abyssinia)",
        methodOption = "አቢሲኒያ ባንክ",
        accountNumber = "66833787",
        ownerName = RECIPIENT_OWNER,
        provider = BankProvider.ABYSSINIA,
      ),
    )
}
