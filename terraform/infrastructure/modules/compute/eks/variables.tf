variable "current_aws_account_arn" {
  description = "The current AWS account ARN"
  type        = string
}

variable "cluster_name" {
  type        = string
  description = "The name to assign to the EKS cluster."
}

variable "cluster_version" {
  type        = string
  description = "The desired Kubernetes version for the EKS cluster (e.g., 1.29)."
}

variable "vpc_id" {
  type        = string
  description = "The ID of the VPC where the EKS cluster and its resources will be deployed."
}

variable "subnet_ids" {
  type        = list(string)
  description = "A list of subnet IDs (typically private subnets) for the EKS control plane and Fargate profiles."
}
