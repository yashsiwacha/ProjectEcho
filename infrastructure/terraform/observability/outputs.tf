output "ecs_log_group_name" {
  value = aws_cloudwatch_log_group.ecs.name
}

output "otel_policy_arn" {
  value = aws_iam_policy.otel_policy.arn
}
