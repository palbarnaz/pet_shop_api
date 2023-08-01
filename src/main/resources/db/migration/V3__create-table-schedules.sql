  create table schedules (
       id uuid primary key,
      "date_hour" timestamp not null,
      id_client uuid not null references users(id),
      id_animal uuid not null references animals(id)
   );
