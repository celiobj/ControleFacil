CREATE OR REPLACE FUNCTION touch_property_activity() RETURNS trigger AS $$
DECLARE
  affected_property_id text;
  activity_timestamp timestamp(3) without time zone;
BEGIN
  activity_timestamp := CURRENT_TIMESTAMP AT TIME ZONE 'UTC';

  IF TG_TABLE_NAME = 'checklist_items' THEN
    IF TG_OP <> 'INSERT' THEN
      SELECT property_id INTO affected_property_id
      FROM property_checklists
      WHERE id = OLD.checklist_id;
      UPDATE properties SET updated_at = activity_timestamp
      WHERE id = affected_property_id;
    END IF;
    IF TG_OP <> 'DELETE' THEN
      SELECT property_id INTO affected_property_id
      FROM property_checklists
      WHERE id = NEW.checklist_id;
      UPDATE properties SET updated_at = activity_timestamp
      WHERE id = affected_property_id;
    END IF;
  ELSIF TG_TABLE_NAME = 'regularization_tasks' THEN
    IF TG_OP <> 'INSERT' THEN
      SELECT property_id INTO affected_property_id
      FROM property_regularizations
      WHERE id = OLD.regularization_id;
      UPDATE properties SET updated_at = activity_timestamp
      WHERE id = affected_property_id;
    END IF;
    IF TG_OP <> 'DELETE' THEN
      SELECT property_id INTO affected_property_id
      FROM property_regularizations
      WHERE id = NEW.regularization_id;
      UPDATE properties SET updated_at = activity_timestamp
      WHERE id = affected_property_id;
    END IF;
  ELSE
    IF TG_OP <> 'INSERT' THEN
      UPDATE properties SET updated_at = activity_timestamp
      WHERE id = OLD.property_id;
    END IF;
    IF TG_OP <> 'DELETE' THEN
      UPDATE properties SET updated_at = activity_timestamp
      WHERE id = NEW.property_id;
    END IF;
  END IF;

  RETURN NULL;
END;
$$ LANGUAGE plpgsql;

UPDATE properties
SET updated_at = GREATEST(updated_at, created_at)
WHERE updated_at < created_at;