INSERT INTO lesson_group_statuses (status)
    VALUES ('LESSON_GROUP_ACTIVE'), ('LESSON_GROUP_INACTIVE');

INSERT INTO lesson_statuses (status)
    VALUES ('LESSON_COMING'), ('LESSON_PAST'), ('LESSON_CANCELED');

INSERT INTO location_statuses (status)
    VALUES ('LOCATION_ACTIVE'), ('LOCATION_INACTIVE');

INSERT INTO payment_statuses (status)
    VALUES ('PAYMENT_PAID'), ('PAYMENT_UNPAID'), ('PAYMENT_CANCELED'), ('PAYMENT_REMOVED');

INSERT INTO request_student_statuses (status)
    VALUES ('REQUEST_NEW'), ('REQUEST_BLOCKED'), ('REQUEST_FINISHED'), ('REQUEST_CANCELED');

INSERT INTO student_statuses (status)
    VALUES ('STUDENT_ACTIVE'), ('STUDENT_INACTIVE');

INSERT INTO roles (id, name)
    VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN'), (3, 'ROLE_SUPERADMIN');