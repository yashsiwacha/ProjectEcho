terraform {
  required_version = ">= 1.5.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
  default_tags {
    tags = {
      Project     = "ProjectEcho"
      Environment = var.environment
      ManagedBy   = "Terraform"
    }
  }
}

# Local variables mapping
locals {
  name_prefix = "echo-${var.environment}"
}

module "networking" {
  source = "./networking"

  name_prefix = local.name_prefix
  environment = var.environment
  aws_region  = var.aws_region
  vpc_cidr    = var.vpc_cidr
}

module "database" {
  source = "./database"

  name_prefix      = local.name_prefix
  environment      = var.environment
  vpc_id           = module.networking.vpc_id
  database_subnets = module.networking.database_subnets
  db_password      = var.db_password
}

module "alb" {
  source = "./alb"

  name_prefix    = local.name_prefix
  vpc_id         = module.networking.vpc_id
  public_subnets = module.networking.public_subnets
}

module "ecs" {
  source = "./ecs"

  name_prefix       = local.name_prefix
  vpc_id            = module.networking.vpc_id
  private_subnets   = module.networking.private_subnets
  alb_sg_id         = module.alb.alb_sg_id
  frontend_tg_arn   = module.alb.frontend_tg_arn
  backend_tg_arn    = module.alb.backend_tg_arn
  db_password       = var.db_password
  postgres_endpoint = module.database.postgres_endpoint
  redis_endpoint    = module.database.redis_endpoint
}

module "security" {
  source = "./security"

  name_prefix = local.name_prefix
  alb_arn     = module.alb.alb_arn
}

module "observability" {
  source = "./observability"

  name_prefix = local.name_prefix
}
