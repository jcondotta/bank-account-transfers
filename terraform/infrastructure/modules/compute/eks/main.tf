data "aws_caller_identity" "current" {}

module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "20.34.0"

  cluster_name    = var.cluster_name
  cluster_version = var.cluster_version
  vpc_id          = var.vpc_id
  subnet_ids      = var.subnet_ids

  cluster_security_group_name = "eks-bank-account-transfers-cluster-sg"
  node_security_group_name = "eks-bank-account-transfers-node-sg"

  enable_irsa    = true
  authentication_mode = "API"
  access_entries = {
    admin = {
      principal_arn      = data.aws_caller_identity.current.arn
      kubernetes_groups  = ["system:masters"]
      policy_associations = []
    }
  }

  fargate_profiles = {
    default = {
      name = "default"
      selectors = [
        {
          namespace = "default"
        },
        {
          namespace = "kube-system"
        }
      ]
    }
  }

  cluster_addons = {
    coredns = {
      resolve_conflicts = "OVERWRITE"
    }
    kube-proxy = {
      resolve_conflicts = "OVERWRITE"
    }
    vpc-cni = {
      resolve_conflicts = "OVERWRITE"
    }
  }

  fargate_profile_defaults = {
    pod_execution_role_arn = aws_iam_role.eks_fargate_profile_role.arn
  }
}