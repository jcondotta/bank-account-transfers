resource "aws_internet_gateway" "this" {
  vpc_id = aws_vpc.this.id

  tags = {
    Name = "bank-account-transfers-ig-${var.environment}"
  }
}