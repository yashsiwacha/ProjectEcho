variable "name_prefix" {
  type = string
}

variable "vpc_id" {
  type = string
}

variable "private_subnets" {
  type = list(string)
}

variable "alb_sg_id" {
  type = string
}

variable "frontend_tg_arn" {
  type = string
}

variable "backend_tg_arn" {
  type = string
}

variable "db_password" {
  type      = string
  sensitive = true
}

variable "postgres_endpoint" {
  type = string
}

variable "redis_endpoint" {
  type = string
}
