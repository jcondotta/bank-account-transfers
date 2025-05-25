provider "aws" {
  region      = var.aws_region
  max_retries = 5

  default_tags {
    tags = {
      "project"     = "bank-account-transfers",
      "environment" = var.environment,
      "owner"       = var.aws_profile
      "managed-by"  = "terraform"
    }
  }
}