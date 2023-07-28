  alter table schedules add id_service uuid not null references services(id);
