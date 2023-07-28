create table users (
  id UUID NOT NULL,
  name varchar(100) NOT NULL,
  email varchar(100) UNIQUE,
  phone varchar(11) NOT NULL,
  password varchar(250) NOT NULL,
  profile varchar(50)  NOT NULL,
   primary key(id)
);