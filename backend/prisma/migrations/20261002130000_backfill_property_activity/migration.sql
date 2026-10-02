WITH related_activity AS (
  SELECT property_id, changed_at AS activity_at
  FROM property_status_history
  UNION ALL
  SELECT property_id, updated_at
  FROM property_checklists
  UNION ALL
  SELECT regularization.property_id, task.updated_at
  FROM regularization_tasks AS task
  JOIN property_regularizations AS regularization
    ON regularization.id = task.regularization_id
  UNION ALL
  SELECT property_id, updated_at
  FROM property_regularizations
  UNION ALL
  SELECT property_id, updated_at
  FROM acquisitions
  UNION ALL
  SELECT property_id, updated_at
  FROM property_documents
  UNION ALL
  SELECT property_id, updated_at
  FROM property_possessions
  UNION ALL
  SELECT property_id, created_at
  FROM property_events
  UNION ALL
  SELECT property_id, updated_at
  FROM sale_scenarios
), latest_activity AS (
  SELECT property_id, MAX(activity_at) AS activity_at
  FROM related_activity
  GROUP BY property_id
)
UPDATE properties AS property
SET updated_at = activity.activity_at
FROM latest_activity AS activity
WHERE property.id = activity.property_id
  AND activity.activity_at > property.updated_at;