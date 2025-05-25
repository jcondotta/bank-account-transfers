terraform {
  cloud {
    organization = "jcondotta"

    workspaces {
      name = "bank-account-transfers-prod"
    }
  }
}