variable "aws_region" {
  description = "The AWS region where resources will be deployed."
  type        = string
  default     = "us-east-1"
}

variable "environment" {
  description = "The environment to deploy to (e.g. dev, staging, prod)"
  type        = string
  default     = "prod"
}

variable "aws_profile" {
  description = "The AWS profile to use for deployment."
  type        = string
  default     = "jcondotta.admin"
}