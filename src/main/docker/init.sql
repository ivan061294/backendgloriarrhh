create table society
(
    code      char(2)     not null,
    name      varchar(50) not null,
    full_flow boolean     not null,
    color     char(6)     not null,
    constraint pk_society primary key (code)
);

create table division
(
    code char(4)     not null,
    name varchar(50) not null,
    constraint pk_division primary key (code)
);

create table management
(
    id   serial      not null,
    name varchar(50) not null,
    constraint pk_management primary key (id),
    constraint uq_management_name unique (name)
);

create table organizational_unit
(
    code char(8)     not null,
    name varchar(50) not null,
    constraint pk_organizational_unit primary key (code)
);

create table cost_center
(
    code char(9)     not null,
    name varchar(50) not null,
    constraint pk_cost_center primary key (code)
);

create table physical_location
(
    code char(4)     not null,
    name varchar(50) not null,
    constraint pk_physical_location primary key (code)
);

create table post_type
(
    id   serial      not null,
    name varchar(50) not null,
    constraint pk_post_type primary key (id)
);

-- Don't forget to add email for email management
create table employee_sap
(
    society_code                char(2)      not null,
    division_code               char(4)      not null,
    physical_location_code      char(4)      not null,
    -- level 2 management code
    management_level_2_code     char(8)      not null,
    -- level 1 management
    management_id               integer      not null,
    organizational_unit_code    char(8)      not null,
    cost_center_code            char(9)      not null,
    -- primary key
    employee_code               char(8)      not null,
    employee_email              varchar(254) not null,
    employee_first_name         varchar(50)  not null,
    employee_father_family_name varchar(25)  not null,
    employee_mother_family_name varchar(25)  not null,
    -- unique key
    post_code                   char(8)      not null,
    post_type_id                integer      not null,
    start_date                  date         not null,
    superior_employee_code      char(8),
    constraint pk_employee_sap primary key (employee_code),
    constraint uq_employee_sap_post_code unique (post_code),
    constraint fk_employee_sap_society foreign key (society_code) references society (code),
    constraint fk_employee_sap_division foreign key (division_code) references division (code),
    constraint fk_employee_sap_management foreign key (management_id) references management (id),
    constraint fk_employee_sap_organizational_unit foreign key (organizational_unit_code) references organizational_unit (code),
    constraint fk_employee_sap_cost_center foreign key (cost_center_code) references cost_center (code),
    constraint fk_employee_sap_physical_location foreign key (physical_location_code) references physical_location (code),
    constraint fk_employee_sap_post_type foreign key (post_type_id) references post_type (id),
    constraint fk_employee_sap_own foreign key (superior_employee_code) references employee_sap (employee_code)
);

create table post_type_characteristic
(
    id           serial       not null,
    name         varchar(50)  not null,
    value        varchar(254) not null,
    post_type_id integer      not null,
    constraint pk_post_type_characteristic primary key (id),
    constraint fk_post_type_characteristic_post_type foreign key (post_type_id) references post_type (id)
);

create table "user"
(
    employee_code char(8)  not null,
    role          smallint not null,
    -- here should be an AWS Cognito User identifier,
    -- and with the `id` they should be a primary key together.
    -- TODO: user_cognito_id varchar(20) not null
    constraint pk_user primary key (employee_code),
    constraint fk_user_employee_sap foreign key (employee_code) references employee_sap (employee_code)
);

create table human_management_rule
(
    society_code           char(2) not null,
    division_code          char(4) not null,
    physical_location_code char(4) not null,
    user_employee_code     char(8) not null,
    constraint pk_human_management_rule primary key (society_code, division_code, physical_location_code,
                                                     user_employee_code),
    constraint fk_hmr_society foreign key (society_code) references society (code),
    constraint fk_hmr_division foreign key (division_code) references division (code),
    constraint fk_hmr_physical_location foreign key (physical_location_code) references physical_location (code),
    constraint fk_hmr_user foreign key (user_employee_code) references "user" (employee_code)
);

create table request
(
    id                       serial       not null,
    type                     smallint     not null,
    search_type              smallint     not null,
    vacancies                integer      not null,
    estimated_start_date     date         not null,
    considered_in_staff_plan bool         not null,
    contract_type            smallint     not null,
    contract_time            smallint     not null,
    justification            varchar(254) not null,
    experience_time          smallint     not null,
    age_range                smallint     not null,
    genre                    smallint     not null,
    civil_status             smallint     not null,
    request_status           smallint     not null,
    file_key                 varchar(50),
    constraint pk_request primary key (id)
);

create table request_user_actions
(
    request_id         integer                     not null,
    user_employee_code char(8)                     not null,
    action             smallint                    not null,
    action_date        timestamp without time zone not null,
    comment            varchar(254),
    -- Can multiple users make an action over a request? If they can, there should be a serial pk
    constraint pk_request_user_actions primary key (request_id, user_employee_code, action),
    constraint fk_actions_request foreign key (request_id) references request (id),
    constraint fk_actions_user foreign key (user_employee_code) references "user" (employee_code)
);

create table request_post_type
(
    id                       integer not null,
    approved_quantity        integer not null,
    post_type_id             integer not null,
    society_code             char(2) not null,
    division_code            char(4) not null,
    management_id            integer not null,
    organizational_unit_code char(8) not null,
    physical_location_code   char(4) not null,
    cost_center_code         char(9) not null,
    constraint pk_request_post_type primary key (id),
    constraint fk_request_post_type_r foreign key (id) references request (id),
    constraint fk_request_post_type_pt foreign key (post_type_id) references post_type (id),
    constraint fk_request_post_type_s foreign key (society_code) references society (code),
    constraint fk_request_post_type_d foreign key (division_code) references division (code),
    constraint fk_request_post_type_m foreign key (management_id) references management (id),
    constraint fk_request_post_type_ou foreign key (organizational_unit_code) references organizational_unit (code),
    constraint fk_request_post_type_pl foreign key (physical_location_code) references physical_location (code),
    constraint fk_request_post_type_cc foreign key (cost_center_code) references cost_center (code)
);

create table request_post
(
    id         serial  not null,
    post_code  char(8) not null,
    approved   boolean not null,
    request_id integer not null,
    constraint pk_request_post primary key (id),
    constraint fk_request_post_r foreign key (request_id) references request (id),
    constraint fk_request_post_p foreign key (post_code) references employee_sap (post_code)
);

-- Data --
-- Society --
insert into society(code, name, full_flow, color)
values ('10', 'GLORIA S.A.', true, 'c48e00');
insert into society(code, name, full_flow, color)
values ('12', 'PANIFICADORA GLORIA S.A.', true, '0693e3');

-- Division --
insert into division(code, name)
values ('1025', 'GLORIA LIMA');
insert into division(code, name)
values ('1210', 'PANIFICADORA GLORIA S.A');


-- Physical Location --
insert into physical_location(code, name)
values ('1012', 'PLANTA HUACHIPA');
insert into physical_location(code, name)
values ('1212', 'PLANTA HUACHIPA');
insert into physical_location(code, name)
values ('1011', 'EDIFICIO CORPORATIVO');


-- Management --
insert into management(name)
values ('PRESIDENCIA EJECUTIVA');
insert into management(name)
values ('PANIFICADORA GLORIA');
insert into management(name)
values ('DIRECCIÓN CORPORATIVA DE GESTIÓN HUMANA');
insert into management(name)
values ('DIRECCIÓN CORPORATIVA DE FINANZAS');

-- Organizational Unit --
insert into organizational_unit(code, name)
values ('10000041', 'GERENCIA GENERAL');
insert into organizational_unit(code, name)
values ('10011450', 'PANIFICADORA GLORIA');
insert into organizational_unit(code, name)
values ('10011454', 'PANIFICADORA - PRODUCCION');
insert into organizational_unit(code, name)
values ('10001293', 'EFECTIVIDAD ORGANIZACIONAL');
insert into organizational_unit(code, name)
values ('10012764', 'GESTION HUMANA GLORIA');
insert into organizational_unit(code, name)
values ('10000002', 'PRESIDENCIA EJECUTIVA');
insert into organizational_unit(code, name)
values ('10000008', 'DIRECCIÓN CORPORATIVA DE FINANZAS');
insert into organizational_unit(code, name)
values ('10000009', 'FINANZAS - CONTABILIDAD');


-- Cost Center --
insert into cost_center(code, name)
values ('101120102', 'GERENCIA GENERAL');
insert into cost_center(code, name)
values ('121010101', 'JEFATURA DE PRODUCCIÓN');
insert into cost_center(code, name)
values ('121010209', 'PERSONAL PANIFICADORA');
insert into cost_center(code, name)
values ('121010215', 'PERSONAL ENVASADO CAMPAÑA');
insert into cost_center(code, name)
values ('101124010', 'COMPENSACIÓN Y E.O. CORPORATIVA');
insert into cost_center(code, name)
values ('101124012', 'GESTIÓN HUMANA');
insert into cost_center(code, name)
values ('101120101', 'PRESIDENCIA EJECUTIVA');
insert into cost_center(code, name)
values ('101122001', 'FINANZAS CORPORATIVA');
insert into cost_center(code, name)
values ('101122005', 'CONTABILIDAD CORPORATIVA');

-- Post-Type --
insert into post_type(name)
values ('OPERARIO DE PANIFICACIÓN');
insert into post_type(name)
values ('OPERADOR DE PANIFICACIÓN');
insert into post_type(name)
values ('AUXILIAR ADMINISTRATIVO');
insert into post_type(name)
values ('SUPERVISOR DE TURNO');
insert into post_type(name)
values ('SECRETARIA');
insert into post_type(name)
values ('OPERARIO DE ALMACÉN');
insert into post_type(name)
values ('ASISTENTE DE GESTIÓN HUMANA');
insert into post_type(name)
values ('JEFE DE MANTENIMIENTO');
insert into post_type(name)
values ('ELECTRICISTA ELECTRÓNICO');
insert into post_type(name)
values ('ENCARGADO DE ALMACÉN');
insert into post_type(name)
values ('JEFE DE CALIDAD');
insert into post_type(name)
values ('ANALISTA CONTROL DE CALIDAD');
insert into post_type(name)
values ('MECÁNICO DE MANTENIMIENTO');
insert into post_type(name)
values ('INSPECTOR CONTROL DE CALIDAD');
insert into post_type(name)
values ('SUPERINTENDENTE DE PRODUCCIÓN');
insert into post_type(name)
values ('PLANIFICADOR DE PRODUCCIÓN');
insert into post_type(name)
values ('SUPERVISOR CONTROL DE CALIDAD');
insert into post_type(name)
values ('GERENTE GENERAL');
insert into post_type(name)
values ('ANALISTA DE COMPENSACIONES');
insert into post_type(name)
values ('ASISTENTE DE GESTIÓN HUMANA');
insert into post_type(name)
values ('PRESIDENTE EJECUTIVO');
insert into post_type(name)
values ('DIRECTOR CORPORATIVO FINANZAS');
insert into post_type(name)
values ('GERENTE CORPORATIVO DE CONTABILIDAD');

-- Employee --
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('10', '1025', '1012', '0', 1, '10000041', '101120102', '30046325', 'eavila@mail.com', 'ENRIQUE OSMAR', 'AVILA',
        'CAMACHO',
        '20000190', 18, '2020/10/12', null);
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '0', 2, '10011450', '121010101', '30030673', 'sgarcia@mail.com', 'SERGIO ADOLFO',
        'GARCIA', 'ARANCIBIA',
        '20047501', 15, '2016/01/01', '30046325');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010101', '30030712', 'esilverio@mail.com', 'ELMER OSCAR',
        'SILVERIO', 'TAFUR',
        '20047508', 4, '2016/01/01', '30030673');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30030719', 'jlopez@mail.com', 'JORGE LUIS',
        'LOPEZ', 'CORDOVA',
        '20047517', 1, '01/01/16', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30036375', 'vronceros@mail.com', 'VICTOR RAUL',
        'RONCEROS', 'CHIRINOS',
        '20051581', 1, '2017/08/07', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30040413', 'halejo@mail.com',
        'HILDA DEL ROSARIO', 'ALEJO',
        'CASTILLO', '20056596', 1, '2018/08/06', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010215', '30046818', 'jfachin@mail.com', 'JAIRO ALPINO',
        'FACHIN', 'HUAYABAN',
        '20061801', 1, '2021/01/18', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name,
                         post_code, post_type_id, start_date, superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30046820', 'dmolleapaza@mail.com', 'DANTE YHONY',
        'MOLLEAPAZA', 'QUEA',
        '20050622', 1, '2021/01/05', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name,
                         post_code, post_type_id, start_date, superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30037865', 'jsanchez@mail.com', 'JUAN CARLO',
        'SANCHEZ', 'CHAVEZ',
        '20050582', 1, '2017/10/02', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30044827', 'aherrera@mail.com', 'ALEX',
        'HERRERA',
        'HUAMAN', '20050523', 1, '2020/02/10', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30030711', 'jquispe@mail.com', 'JULIA LEONCIA',
        'QUISPE', 'DE LA CRUZ', '20047522', 1, '2016/01/01', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032019', 'ljulica@mail.com', 'LUCHO ELMER',
        'JULCA', 'SIFUENTES', '20048578', 1, '2016/05/14', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30037864', 'jverona@mail.com', 'JONATHAN',
        'VERONA', 'CUNYA', '20050532', 1, '2017/10/02', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30046821', 'rferre@mail.com', 'RAFAEL', 'FERRE',
        'CAMPOS', '20053236', 1, '2021/01/05', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032017', 'jesquivel@mail.com', 'JOSE LUIS',
        'ESQUIVEL', 'NINANYA', '20048576', 1, '2016/05/14', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032664', 'apariona@mail.com', 'ALIPIO',
        'PARIONA', 'BAUTISTA', '20050585', 1, '2016/08/01', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032697', 'mbravo@mail.com', 'MAYCON NAY',
        'BRAVO', 'HERRERA', '20050600', 1, '2016/08/15', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30036371', 'cveramendi@mail.com', 'CLAUDIA',
        'VERAMENDI', 'VERAMENDI', '20050524', 1, '2017/08/07', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010215', '30046819', 'emonasterio@mail.com',
        'EDWIN LENNIN', 'MONASTERIO', 'SEMINARIO', '20061802', 1, '2021/01/18', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032662', 'cmontesinos@mail.com',
        'CARLOS MARTIN',
        'MONTESINOS', 'ZUÑE', '20050583', 1, '2016/08/01', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30036358', 'cinonan@mail.com', 'CARLOS',
        'INOÑAN', 'CHAPOÑAN', '20050520', 1, '2017/08/07', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30036722', 'jvega@mail.com', 'JHOSSY WHELLS',
        'VEGA', 'DOMINGUEZ', '20050604', 1, '2017/08/21', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30040213', 'ccisneros@mail.com', 'CESAR POLANKO',
        'CISNEROS', 'BAUTISTA', '20050533', 1, '2018/07/09', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30040412', 'mayala@mail.com', 'MARISOL MARISA',
        'AYALA', 'ARMES', '20056658', 1, '2018/08/06', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010215', '30046817', 'jlopez@mail.com', 'JOSE ANTONIO',
        'LOPEZ', 'GARCIA', '20061800', 1, '2021/01/18', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30035800', 'denciso@mail.com', 'DESIDERIO',
        'ENCISO', 'VILLALOBOS', '20053237', 1, '2017/05/12', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30040424', 'jortiz@mail.com', 'JULISSA',
        'ORTIZ', 'BARBARAN', '20056653', 1, '2018/08/06', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032020', 'elozada@mail.com', 'ELQUI ANTONIO',
        'LOZADA', 'SALAS', '20048579', 1, '2016/05/14', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032481', 'lcersuolo@mail.com', 'LUIS ALBERTO',
        'CERASUOLO', 'CAVERO', '20050451', 1, '2016/07/20', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name,
                         post_code, post_type_id, start_date, superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032708', 'jlarico@mail.com', 'JAIME DANIEL',
        'LARICO', 'QUISPE', '20050611', 1, '2016/08/15', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30036361', 'lfachin@mail.com', 'LUIS IZAEL',
        'FACHIN', 'AHUANARI', '20047513', 1, '2017/08/07', '30030712');
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('10', '1025', '1011', '10011454', 3, '10001293', '101124010', '30001416', 'llopez@mail.com', 'LUIS MIGUEL',
        'LÓPEZ', 'GÓMEZ', '20029073', 19, '2011/08/23', null);
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('10', '1025', '1012', '10011454', 1, '10012764', '101124012', '30009554', 'calvarez@mail.com', 'CARLOS',
        'ÁLVAREZ', 'PÉREZ', '20000197', 20, '2010/07/01', null);
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('10', '1025', '1011', '10011454', 1, '10012764', '101120101', '30025842', 'phuaman@mail.com', 'PRESIDENCIO',
        'HUAMÁN', 'PATRICIO', '20000000', 21, '2014/03/08', null);
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('10', '1025', '1011', '10011454', 4, '10000008', '101122001', '30041624', 'jvasquez@mail.com', 'JORGE LUIS',
        'VÁSQUEZ', 'CHÁVEZ', '20000011', 22, '2019/01/28', null);
insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,
                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,
                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,
                         superior_employee_code)
values ('10', '1025', '1011', '10011454', 4, '10000009', '101122005', '30000218', 'cgarcia@mail.com', 'CESAR',
        'GARCÍA', 'LUPANA', '20005292', 22, '2017/09/01', '30041624');

-- User --
insert into "user"(employee_code, role)
values ('30036361', 0);
insert into "user"(employee_code, role)
values ('30030712', 0);
insert into "user"(employee_code, role)
values ('30030673', 0);
insert into "user"(employee_code, role)
values ('30044827', 1);
insert into "user"(employee_code, role)
values ('30036358', 7);
insert into "user"(employee_code, role)
values ('30046325', 2);
insert into "user"(employee_code, role)
values ('30001416', 4);
insert into "user"(employee_code, role)
values ('30009554', 5);
insert into "user"(employee_code, role)
values ('30025842', 6);
