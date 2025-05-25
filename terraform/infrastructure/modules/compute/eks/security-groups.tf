# resource "aws_security_group" "eks_cluster_sg" {
#   name        = "eks-cluster-sg"
#   description = "Security group for EKS control plane"
#   vpc_id      = module.vpc.vpc_id
#
#   ingress {
#     from_port   = 443
#     to_port     = 443
#     protocol    = "tcp"
#     cidr_blocks = ["0.0.0.0/0"] # Adjust for least privilege
#   }
#
#   egress {
#     from_port   = 0
#     to_port     = 0
#     protocol    = "-1"
#     cidr_blocks = ["0.0.0.0/0"]
#   }
# }
#
# resource "aws_security_group" "eks_node_sg" {
#   name        = "eks-node-sg"
#   description = "Security group for EKS worker nodes"
#   vpc_id      = module.vpc.vpc_id
#
#   ingress {
#     description = "Allow all traffic from within the node group"
#     from_port   = 0
#     to_port     = 0
#     protocol    = "-1"
#     self        = true
#   }
#
#   ingress {
#     description = "Allow worker to communicate with control plane"
#     from_port   = 443
#     to_port     = 443
#     protocol    = "tcp"
#     security_groups = [aws_security_group.eks_cluster_sg.id]
#   }
#
#   egress {
#     from_port   = 0
#     to_port     = 0
#     protocol    = "-1"
#     cidr_blocks = ["0.0.0.0/0"]
#   }
# }