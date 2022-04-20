create table lesson_durations (
   id  bigserial not null,
    name varchar(255),
    time time not null,
    primary key (id)
);


create table lesson_group_statuses (
   id  bigserial not null,
    status varchar(22) not null,
    primary key (id)
);


create table lesson_groups (
   id  bigserial not null,
    name varchar(255) not null,
    status_id int8,
    primary key (id)
);


create table lesson_statuses (
   id  bigserial not null,
    status varchar(20) not null,
    primary key (id)
);


create table lessons (
   id  bigserial not null,
    date date not null,
    time time not null,
    duration_id int8,
    lesson_group_id int8,
    location_id int8,
    status_id int8,
    teacher_id int8,
    primary key (id)
);


create table location_statuses (
   id  bigserial not null,
    status varchar(20) not null,
    primary key (id)
);


create table locations (
   id  bigserial not null,
    address varchar(255) not null,
    name varchar(255),
    status_id int8,
    primary key (id)
);


create table payment_amounts (
   id  bigserial not null,
    name varchar(255),
    sum float4 not null,
    primary key (id)
);


create table payment_statuses (
   id  bigserial not null,
    status varchar(20) not null,
    primary key (id)
);


create table payments (
   id  bigserial not null,
    pay_date date not null,
    pay_time time not null,
    amount_id int8,
    lesson_id int8,
    status_id int8,
    student_id int8,
    primary key (id)
);


create table request_student_statuses (
   id  bigserial not null,
    status varchar(20) not null,
    primary key (id)
);


create table request_students (
   id  bigserial not null,
    name varchar(255) not null,
    phone varchar(255) not null,
    location_id int8,
    status_id int8,
    primary key (id)
);


create table roles (
   id  bigserial not null,
    name varchar(20),
    primary key (id)
);


create table student_statuses (
   id  bigserial not null,
    status varchar(20) not null,
    primary key (id)
);


create table students (
   id  bigserial not null,
    description varchar(255),
    name varchar(255),
    parent_name varchar(255) not null,
    parent_phone varchar(255) not null,
    phone varchar(255),
    status_id int8,
    primary key (id)
);


create table students_groups (
   lesson_group_id int8 not null,
    student_id int8 not null,
    primary key (lesson_group_id, student_id)
);


create table user_roles (
   user_id int8 not null,
    role_id int8 not null,
    primary key (user_id, role_id)
);


create table users (
   id  bigserial not null,
    email varchar(255) not null,
    is_active boolean not null,
    password varchar(255) not null,
    username varchar(255),
    primary key (id)
);


alter table lesson_durations
   add constraint lesson_durations_unique_time unique (time);


alter table lesson_groups
   add constraint lesson_groups_unique_name unique (name);


alter table locations
   add constraint locations_unique_address unique (address);


alter table payment_amounts
   add constraint payment_amounts_unique_sum unique (sum);


alter table users
   add constraint users_unique_email unique (email);


alter table lesson_groups
   add constraint lesson_groups_lesson_group_statuses_fk
   foreign key (status_id)
   references lesson_group_statuses;


alter table lessons
   add constraint lessons_lesson_durations_fk
   foreign key (duration_id)
   references lesson_durations;


alter table lessons
   add constraint lessons_lesson_groups_fk
   foreign key (lesson_group_id)
   references lesson_groups;


alter table lessons
   add constraint lessons_locations_fk
   foreign key (location_id)
   references locations;


alter table lessons
   add constraint lessons_lesson_statuses_fk
   foreign key (status_id)
   references lesson_statuses;


alter table lessons
   add constraint lessons_users_fk
   foreign key (teacher_id)
   references users;


alter table locations
   add constraint locations_location_statuses_fk
   foreign key (status_id)
   references location_statuses;


alter table payments
   add constraint payments_payment_amounts_fk
   foreign key (amount_id)
   references payment_amounts;


alter table payments
   add constraint payments_lessons_fk
   foreign key (lesson_id)
   references lessons;


alter table payments
   add constraint payments_payment_statuses_fk
   foreign key (status_id)
   references payment_statuses;


alter table payments
   add constraint payments_students_fk
   foreign key (student_id)
   references students;


alter table request_students
   add constraint request_students_locations_fk
   foreign key (location_id)
   references locations;


alter table request_students
   add constraint request_students_request_student_statuses_fk
   foreign key (status_id)
   references request_student_statuses;


alter table students
   add constraint students_student_statuses_fk
   foreign key (status_id)
   references student_statuses;


alter table students_groups
   add constraint students_groups_students_fk
   foreign key (student_id)
   references students;


alter table students_groups
   add constraint students_groups_lesson_groups_fk
   foreign key (lesson_group_id)
   references lesson_groups;


alter table user_roles
   add constraint user_roles_roles_fk
   foreign key (role_id)
   references roles;


alter table user_roles
   add constraint user_roles_users_fk
   foreign key (user_id)
   references users;