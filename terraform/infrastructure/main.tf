module "networking" {
  source = "./modules/networking"

  aws_region  = var.aws_region
  environment = var.environment
}

module "eks" {
  source = "./modules/compute/eks"

  current_aws_account_arn = data.aws_caller_identity.current.arn

  cluster_name    = "bank-account-transfers-eks"
  cluster_version = "1.32"

  vpc_id     = module.networking.vpc_id
  subnet_ids = module.networking.private_subnet_ids
}