ALTER TABLE SPACECRAFTS
ADD CONSTRAINT uq_spacecrafts_name_series UNIQUE (name, series);