CREATE OR REPLACE FUNCTION touch_property_activity() RETURNS trigger AS $$
DECLARE
  affected_property_id text;
BEGIN
  IF TG_TABLE_NAME = 'checklist_items' THEN
    IF TG_OP <> 'INSERT' THEN
      SELECT property_id INTO affected_property_id
      FROM property_checklists
      WHERE id = OLD.checklist_id;
      UPDATE properties SET updated_at = CURRENT_TIMESTAMP
      WHERE id = affected_property_id;
    END IF;
    IF TG_OP <> 'DELETE' THEN
      SELECT property_id INTO affected_property_id
      FROM property_checklists
      WHERE id = NEW.checklist_id;
      UPDATE properties SET updated_at = CURRENT_TIMESTAMP
      WHERE id = affected_property_id;
    END IF;
  ELSIF TG_TABLE_NAME = 'regularization_tasks' THEN
    IF TG_OP <> 'INSERT' THEN
      SELECT property_id INTO affected_property_id
      FROM property_regularizations
      WHERE id = OLD.regularization_id;
      UPDATE properties SET updated_at = CURRENT_TIMESTAMP
      WHERE id = affected_property_id;
    END IF;
    IF TG_OP <> 'DELETE' THEN
      SELECT property_id INTO affected_property_id
      FROM property_regularizations
      WHERE id = NEW.regularization_id;
      UPDATE properties SET updated_at = CURRENT_TIMESTAMP
      WHERE id = affected_property_id;
    END IF;
  ELSE
    IF TG_OP <> 'INSERT' THEN
      UPDATE properties SET updated_at = CURRENT_TIMESTAMP
      WHERE id = OLD.property_id;
    END IF;
    IF TG_OP <> 'DELETE' THEN
      UPDATE properties SET updated_at = CURRENT_TIMESTAMP
      WHERE id = NEW.property_id;
    END IF;
  END IF;

  RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER property_status_history_activity
AFTER INSERT OR UPDATE OR DELETE ON property_status_history
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();

CREATE TRIGGER property_checklists_activity
AFTER INSERT OR UPDATE OR DELETE ON property_checklists
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();

CREATE TRIGGER checklist_items_activity
AFTER INSERT OR UPDATE OR DELETE ON checklist_items
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();

CREATE TRIGGER auctions_activity
AFTER INSERT OR UPDATE OR DELETE ON auctions
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();

CREATE TRIGGER expenses_activity
AFTER INSERT OR UPDATE OR DELETE ON expenses
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();

CREATE TRIGGER renovations_activity
AFTER INSERT OR UPDATE OR DELETE ON renovations
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();

CREATE TRIGGER sales_activity
AFTER INSERT OR UPDATE OR DELETE ON sales
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();

CREATE TRIGGER acquisitions_activity
AFTER INSERT OR UPDATE OR DELETE ON acquisitions
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();

CREATE TRIGGER property_documents_activity
AFTER INSERT OR UPDATE OR DELETE ON property_documents
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();

CREATE TRIGGER property_regularizations_activity
AFTER INSERT OR UPDATE OR DELETE ON property_regularizations
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();

CREATE TRIGGER regularization_tasks_activity
AFTER INSERT OR UPDATE OR DELETE ON regularization_tasks
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();

CREATE TRIGGER property_possessions_activity
AFTER INSERT OR UPDATE OR DELETE ON property_possessions
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();

CREATE TRIGGER property_events_activity
AFTER INSERT OR UPDATE OR DELETE ON property_events
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();

CREATE TRIGGER sale_scenarios_activity
AFTER INSERT OR UPDATE OR DELETE ON sale_scenarios
FOR EACH ROW EXECUTE FUNCTION touch_property_activity();