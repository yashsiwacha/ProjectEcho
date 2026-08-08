# VPC and Networking Setup

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.1.2"

  name = "${var.name_prefix}-vpc"
  cidr = var.vpc_cidr

  azs             = ["${var.aws_region}a", "${var.aws_region}b"]
  private_subnets = [cidrsubnet(var.vpc_cidr, 8, 1), cidrsubnet(var.vpc_cidr, 8, 2)]
  public_subnets  = [cidrsubnet(var.vpc_cidr, 8, 3), cidrsubnet(var.vpc_cidr, 8, 4)]
  database_subnets = [cidrsubnet(var.vpc_cidr, 8, 5), cidrsubnet(var.vpc_cidr, 8, 6)]

  enable_nat_gateway = true
  single_nat_gateway = true
  enable_dns_hostnames = true
  enable_dns_support   = true

  create_database_subnet_group = true

  tags = {
    Environment = var.environment
    Project     = "ProjectEcho"
  }
}
