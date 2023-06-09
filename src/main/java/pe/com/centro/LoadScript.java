package pe.com.centro;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static pe.com.centro.configuration.BaseDAO.close;
import static pe.com.centro.configuration.BaseDAO.getConnection;
import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;

// TODO: to be removed
@Slf4j
public class LoadScript implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final String sql = "create table society(" +
            "    code      char(2)     not null," +
            "    name      varchar(50) not null," +
            "    full_flow boolean     not null," +
            "    color     char(6)     not null," +
            "    constraint pk_society primary key (code)\n" +
            ");\n" +
            "\n" +
            "create table division\n" +
            "(\n" +
            "    code char(4)     not null,\n" +
            "    name varchar(50) not null,\n" +
            "    constraint pk_division primary key (code)\n" +
            ");\n" +
            "\n" +
            "create table management\n" +
            "(\n" +
            "    id   serial      not null,\n" +
            "    name varchar(50) not null,\n" +
            "    constraint pk_management primary key (id),\n" +
            "    constraint uq_management_name unique (name)\n" +
            ");\n" +
            "\n" +
            "create table organizational_unit\n" +
            "(\n" +
            "    code char(8)     not null,\n" +
            "    name varchar(50) not null,\n" +
            "    constraint pk_organizational_unit primary key (code)\n" +
            ");\n" +
            "\n" +
            "create table cost_center\n" +
            "(\n" +
            "    code char(9)     not null,\n" +
            "    name varchar(50) not null,\n" +
            "    constraint pk_cost_center primary key (code)\n" +
            ");\n" +
            "\n" +
            "create table physical_location\n" +
            "(\n" +
            "    code char(4)     not null,\n" +
            "    name varchar(50) not null,\n" +
            "    constraint pk_physical_location primary key (code)\n" +
            ");\n" +
            "\n" +
            "create table post_type\n" +
            "(\n" +
            "    id   serial      not null,\n" +
            "    name varchar(50) not null,\n" +
            "    constraint pk_post_type primary key (id)\n" +
            ");\n" +
            "\n" +
            "-- Don't forget to add email for email management\n" +
            "create table employee_sap\n" +
            "(\n" +
            "    society_code                char(2)      not null,\n" +
            "    division_code               char(4)      not null,\n" +
            "    physical_location_code      char(4)      not null,\n" +
            "    -- level 2 management code\n" +
            "    management_level_2_code     char(8)      not null,\n" +
            "    -- level 1 management\n" +
            "    management_id               integer      not null,\n" +
            "    organizational_unit_code    char(8)      not null,\n" +
            "    cost_center_code            char(9)      not null,\n" +
            "    -- primary key\n" +
            "    employee_code               char(8)      not null,\n" +
            "    employee_email              varchar(254) not null,\n" +
            "    employee_first_name         varchar(50)  not null,\n" +
            "    employee_father_family_name varchar(25)  not null,\n" +
            "    employee_mother_family_name varchar(25)  not null,\n" +
            "    -- unique key\n" +
            "    post_code                   char(8)      not null,\n" +
            "    post_type_id                integer      not null,\n" +
            "    start_date                  date         not null,\n" +
            "    superior_employee_code      char(8),\n" +
            "    constraint pk_employee_sap primary key (employee_code),\n" +
            "    constraint uq_employee_sap_post_code unique (post_code),\n" +
            "    constraint fk_employee_sap_society foreign key (society_code) references society (code),\n" +
            "    constraint fk_employee_sap_division foreign key (division_code) references division (code),\n" +
            "    constraint fk_employee_sap_management foreign key (management_id) references management (id),\n" +
            "    constraint fk_employee_sap_organizational_unit foreign key (organizational_unit_code) references organizational_unit (code),\n" +
            "    constraint fk_employee_sap_cost_center foreign key (cost_center_code) references cost_center (code),\n" +
            "    constraint fk_employee_sap_physical_location foreign key (physical_location_code) references physical_location (code),\n" +
            "    constraint fk_employee_sap_post_type foreign key (post_type_id) references post_type (id),\n" +
            "    constraint fk_employee_sap_own foreign key (superior_employee_code) references employee_sap (employee_code)\n" +
            ");\n" +
            "\n" +
            "create table post_type_characteristic\n" +
            "(\n" +
            "    id           serial       not null,\n" +
            "    name         varchar(50)  not null,\n" +
            "    value        varchar(254) not null,\n" +
            "    post_type_id integer      not null,\n" +
            "    constraint pk_post_type_characteristic primary key (id),\n" +
            "    constraint fk_post_type_characteristic_post_type foreign key (post_type_id) references post_type (id)\n" +
            ");\n" +
            "\n" +
            "create table \"user\"\n" +
            "(\n" +
            "    employee_code char(8)  not null,\n" +
            "    role          smallint not null,\n" +
            "    -- here should be an AWS Cognito User identifier,\n" +
            "    -- and with the `id` they should be a primary key together.\n" +
            "    -- TODO: user_cognito_id varchar(20) not null\n" +
            "    constraint pk_user primary key (employee_code),\n" +
            "    constraint fk_user_employee_sap foreign key (employee_code) references employee_sap (employee_code)\n" +
            ");\n" +
            "\n" +
            "create table human_management_rule\n" +
            "(\n" +
            "    society_code           char(2) not null,\n" +
            "    division_code          char(4) not null,\n" +
            "    physical_location_code char(4) not null,\n" +
            "    user_employee_code     char(8) not null,\n" +
            "    constraint pk_human_management_rule primary key (society_code, division_code, physical_location_code,\n" +
            "                                                     user_employee_code),\n" +
            "    constraint fk_hmr_society foreign key (society_code) references society (code),\n" +
            "    constraint fk_hmr_division foreign key (division_code) references division (code),\n" +
            "    constraint fk_hmr_physical_location foreign key (physical_location_code) references physical_location (code),\n" +
            "    constraint fk_hmr_user foreign key (user_employee_code) references \"user\" (employee_code)\n" +
            ");\n" +
            "\n" +
            "create table request\n" +
            "(\n" +
            "    id                       serial       not null,\n" +
            "    type                     smallint     not null,\n" +
            "    search_type              smallint     not null,\n" +
            "    vacancies                integer      not null,\n" +
            "    estimated_start_date     date         not null,\n" +
            "    considered_in_staff_plan bool         not null,\n" +
            "    contract_type            smallint     not null,\n" +
            "    contract_time            smallint     not null,\n" +
            "    justification            varchar(254) not null,\n" +
            "    experience_time          smallint     not null,\n" +
            "    age_range                smallint     not null,\n" +
            "    genre                    smallint     not null,\n" +
            "    civil_status             smallint     not null,\n" +
            "    request_status           smallint     not null,\n" +
            "    file_key                 varchar(50),\n" +
            "    constraint pk_request primary key (id)\n" +
            ");\n" +
            "\n" +
            "create table request_user_actions\n" +
            "(\n" +
            "    request_id         integer                     not null,\n" +
            "    user_employee_code char(8)                     not null,\n" +
            "    action             smallint                    not null,\n" +
            "    action_date        timestamp without time zone not null,\n" +
            "    comment            varchar(254),\n" +
            "    -- Can multiple users make an action over a request? If they can, there should be a serial pk\n" +
            "    constraint pk_request_user_actions primary key (request_id, user_employee_code, action),\n" +
            "    constraint fk_actions_request foreign key (request_id) references request (id),\n" +
            "    constraint fk_actions_user foreign key (user_employee_code) references \"user\" (employee_code)\n" +
            ");\n" +
            "\n" +
            "create table request_post_type\n" +
            "(\n" +
            "    id                       integer not null,\n" +
            "    approved_quantity        integer not null,\n" +
            "    post_type_id             integer not null,\n" +
            "    society_code             char(2) not null,\n" +
            "    division_code            char(4) not null,\n" +
            "    management_id            integer not null,\n" +
            "    organizational_unit_code char(8) not null,\n" +
            "    physical_location_code   char(4) not null,\n" +
            "    cost_center_code         char(9) not null,\n" +
            "    constraint pk_request_post_type primary key (id),\n" +
            "    constraint fk_request_post_type_r foreign key (id) references request (id),\n" +
            "    constraint fk_request_post_type_pt foreign key (post_type_id) references post_type (id),\n" +
            "    constraint fk_request_post_type_s foreign key (society_code) references society (code),\n" +
            "    constraint fk_request_post_type_d foreign key (division_code) references division (code),\n" +
            "    constraint fk_request_post_type_m foreign key (management_id) references management (id),\n" +
            "    constraint fk_request_post_type_ou foreign key (organizational_unit_code) references organizational_unit (code),\n" +
            "    constraint fk_request_post_type_pl foreign key (physical_location_code) references physical_location (code),\n" +
            "    constraint fk_request_post_type_cc foreign key (cost_center_code) references cost_center (code)\n" +
            ");\n" +
            "\n" +
            "create table request_post\n" +
            "(\n" +
            "    id         serial  not null,\n" +
            "    post_code  char(8) not null,\n" +
            "    approved   boolean not null,\n" +
            "    request_id integer not null,\n" +
            "    constraint pk_request_post primary key (id),\n" +
            "    constraint fk_request_post_r foreign key (request_id) references request (id),\n" +
            "    constraint fk_request_post_p foreign key (post_code) references employee_sap (post_code)\n" +
            ");\n" +
            "\n" +
            "-- Data --\n" +
            "-- Society --\n" +
            "insert into society(code, name, full_flow, color)\n" +
            "values ('10', 'GLORIA S.A.', true, 'c48e00');\n" +
            "insert into society(code, name, full_flow, color)\n" +
            "values ('12', 'PANIFICADORA GLORIA S.A.', true, '0693e3');\n" +
            "\n" +
            "-- Division --\n" +
            "insert into division(code, name)\n" +
            "values ('1025', 'GLORIA LIMA');\n" +
            "insert into division(code, name)\n" +
            "values ('1210', 'PANIFICADORA GLORIA S.A');\n" +
            "\n" +
            "\n" +
            "-- Physical Location --\n" +
            "insert into physical_location(code, name)\n" +
            "values ('1012', 'PLANTA HUACHIPA');\n" +
            "insert into physical_location(code, name)\n" +
            "values ('1212', 'PLANTA HUACHIPA');\n" +
            "insert into physical_location(code, name)\n" +
            "values ('1011', 'EDIFICIO CORPORATIVO');\n" +
            "\n" +
            "\n" +
            "-- Management --\n" +
            "insert into management(name)\n" +
            "values ('PRESIDENCIA EJECUTIVA');\n" +
            "insert into management(name)\n" +
            "values ('PANIFICADORA GLORIA');\n" +
            "insert into management(name)\n" +
            "values ('DIRECCIÓN CORPORATIVA DE GESTIÓN HUMANA');\n" +
            "insert into management(name)\n" +
            "values ('DIRECCIÓN CORPORATIVA DE FINANZAS');\n" +
            "\n" +
            "-- Organizational Unit --\n" +
            "insert into organizational_unit(code, name)\n" +
            "values ('10000041', 'GERENCIA GENERAL');\n" +
            "insert into organizational_unit(code, name)\n" +
            "values ('10011450', 'PANIFICADORA GLORIA');\n" +
            "insert into organizational_unit(code, name)\n" +
            "values ('10011454', 'PANIFICADORA - PRODUCCION');\n" +
            "insert into organizational_unit(code, name)\n" +
            "values ('10001293', 'EFECTIVIDAD ORGANIZACIONAL');\n" +
            "insert into organizational_unit(code, name)\n" +
            "values ('10012764', 'GESTION HUMANA GLORIA');\n" +
            "insert into organizational_unit(code, name)\n" +
            "values ('10000002', 'PRESIDENCIA EJECUTIVA');\n" +
            "insert into organizational_unit(code, name)\n" +
            "values ('10000008', 'DIRECCIÓN CORPORATIVA DE FINANZAS');\n" +
            "insert into organizational_unit(code, name)\n" +
            "values ('10000009', 'FINANZAS - CONTABILIDAD');\n" +
            "\n" +
            "\n" +
            "-- Cost Center --\n" +
            "insert into cost_center(code, name)\n" +
            "values ('101120102', 'GERENCIA GENERAL');\n" +
            "insert into cost_center(code, name)\n" +
            "values ('121010101', 'JEFATURA DE PRODUCCIÓN');\n" +
            "insert into cost_center(code, name)\n" +
            "values ('121010209', 'PERSONAL PANIFICADORA');\n" +
            "insert into cost_center(code, name)\n" +
            "values ('121010215', 'PERSONAL ENVASADO CAMPAÑA');\n" +
            "insert into cost_center(code, name)\n" +
            "values ('101124010', 'COMPENSACIÓN Y E.O. CORPORATIVA');\n" +
            "insert into cost_center(code, name)\n" +
            "values ('101124012', 'GESTIÓN HUMANA');\n" +
            "insert into cost_center(code, name)\n" +
            "values ('101120101', 'PRESIDENCIA EJECUTIVA');\n" +
            "insert into cost_center(code, name)\n" +
            "values ('101122001', 'FINANZAS CORPORATIVA');\n" +
            "insert into cost_center(code, name)\n" +
            "values ('101122005', 'CONTABILIDAD CORPORATIVA');\n" +
            "\n" +
            "-- Post-Type --\n" +
            "insert into post_type(name)\n" +
            "values ('OPERARIO DE PANIFICACIÓN');\n" +
            "insert into post_type(name)\n" +
            "values ('OPERADOR DE PANIFICACIÓN');\n" +
            "insert into post_type(name)\n" +
            "values ('AUXILIAR ADMINISTRATIVO');\n" +
            "insert into post_type(name)\n" +
            "values ('SUPERVISOR DE TURNO');\n" +
            "insert into post_type(name)\n" +
            "values ('SECRETARIA');\n" +
            "insert into post_type(name)\n" +
            "values ('OPERARIO DE ALMACÉN');\n" +
            "insert into post_type(name)\n" +
            "values ('ASISTENTE DE GESTIÓN HUMANA');\n" +
            "insert into post_type(name)\n" +
            "values ('JEFE DE MANTENIMIENTO');\n" +
            "insert into post_type(name)\n" +
            "values ('ELECTRICISTA ELECTRÓNICO');\n" +
            "insert into post_type(name)\n" +
            "values ('ENCARGADO DE ALMACÉN');\n" +
            "insert into post_type(name)\n" +
            "values ('JEFE DE CALIDAD');\n" +
            "insert into post_type(name)\n" +
            "values ('ANALISTA CONTROL DE CALIDAD');\n" +
            "insert into post_type(name)\n" +
            "values ('MECÁNICO DE MANTENIMIENTO');\n" +
            "insert into post_type(name)\n" +
            "values ('INSPECTOR CONTROL DE CALIDAD');\n" +
            "insert into post_type(name)\n" +
            "values ('SUPERINTENDENTE DE PRODUCCIÓN');\n" +
            "insert into post_type(name)\n" +
            "values ('PLANIFICADOR DE PRODUCCIÓN');\n" +
            "insert into post_type(name)\n" +
            "values ('SUPERVISOR CONTROL DE CALIDAD');\n" +
            "insert into post_type(name)\n" +
            "values ('GERENTE GENERAL');\n" +
            "insert into post_type(name)\n" +
            "values ('ANALISTA DE COMPENSACIONES');\n" +
            "insert into post_type(name)\n" +
            "values ('ASISTENTE DE GESTIÓN HUMANA');\n" +
            "insert into post_type(name)\n" +
            "values ('PRESIDENTE EJECUTIVO');\n" +
            "insert into post_type(name)\n" +
            "values ('DIRECTOR CORPORATIVO FINANZAS');\n" +
            "insert into post_type(name)\n" +
            "values ('GERENTE CORPORATIVO DE CONTABILIDAD');\n" +
            "\n" +
            "-- Employee --\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('10', '1025', '1012', '0', 1, '10000041', '101120102', '30046325', 'eavila@mail.com', 'ENRIQUE OSMAR', 'AVILA',\n" +
            "        'CAMACHO',\n" +
            "        '20000190', 18, '2020/10/12', null);\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '0', 2, '10011450', '121010101', '30030673', 'sgarcia@mail.com', 'SERGIO ADOLFO',\n" +
            "        'GARCIA', 'ARANCIBIA',\n" +
            "        '20047501', 15, '2016/01/01', '30046325');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010101', '30030712', 'esilverio@mail.com', 'ELMER OSCAR',\n" +
            "        'SILVERIO', 'TAFUR',\n" +
            "        '20047508', 4, '2016/01/01', '30030673');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30030719', 'jlopez@mail.com', 'JORGE LUIS',\n" +
            "        'LOPEZ', 'CORDOVA',\n" +
            "        '20047517', 1, '01/01/16', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30036375', 'vronceros@mail.com', 'VICTOR RAUL',\n" +
            "        'RONCEROS', 'CHIRINOS',\n" +
            "        '20051581', 1, '2017/08/07', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30040413', 'halejo@mail.com',\n" +
            "        'HILDA DEL ROSARIO', 'ALEJO',\n" +
            "        'CASTILLO', '20056596', 1, '2018/08/06', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010215', '30046818', 'jfachin@mail.com', 'JAIRO ALPINO',\n" +
            "        'FACHIN', 'HUAYABAN',\n" +
            "        '20061801', 1, '2021/01/18', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name,\n" +
            "                         post_code, post_type_id, start_date, superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30046820', 'dmolleapaza@mail.com', 'DANTE YHONY',\n" +
            "        'MOLLEAPAZA', 'QUEA',\n" +
            "        '20050622', 1, '2021/01/05', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name,\n" +
            "                         post_code, post_type_id, start_date, superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30037865', 'jsanchez@mail.com', 'JUAN CARLO',\n" +
            "        'SANCHEZ', 'CHAVEZ',\n" +
            "        '20050582', 1, '2017/10/02', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30044827', 'aherrera@mail.com', 'ALEX',\n" +
            "        'HERRERA',\n" +
            "        'HUAMAN', '20050523', 1, '2020/02/10', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30030711', 'jquispe@mail.com', 'JULIA LEONCIA',\n" +
            "        'QUISPE', 'DE LA CRUZ', '20047522', 1, '2016/01/01', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032019', 'ljulica@mail.com', 'LUCHO ELMER',\n" +
            "        'JULCA', 'SIFUENTES', '20048578', 1, '2016/05/14', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30037864', 'jverona@mail.com', 'JONATHAN',\n" +
            "        'VERONA', 'CUNYA', '20050532', 1, '2017/10/02', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30046821', 'rferre@mail.com', 'RAFAEL', 'FERRE',\n" +
            "        'CAMPOS', '20053236', 1, '2021/01/05', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032017', 'jesquivel@mail.com', 'JOSE LUIS',\n" +
            "        'ESQUIVEL', 'NINANYA', '20048576', 1, '2016/05/14', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032664', 'apariona@mail.com', 'ALIPIO',\n" +
            "        'PARIONA', 'BAUTISTA', '20050585', 1, '2016/08/01', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032697', 'mbravo@mail.com', 'MAYCON NAY',\n" +
            "        'BRAVO', 'HERRERA', '20050600', 1, '2016/08/15', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30036371', 'cveramendi@mail.com', 'CLAUDIA',\n" +
            "        'VERAMENDI', 'VERAMENDI', '20050524', 1, '2017/08/07', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010215', '30046819', 'emonasterio@mail.com',\n" +
            "        'EDWIN LENNIN', 'MONASTERIO', 'SEMINARIO', '20061802', 1, '2021/01/18', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032662', 'cmontesinos@mail.com',\n" +
            "        'CARLOS MARTIN',\n" +
            "        'MONTESINOS', 'ZUÑE', '20050583', 1, '2016/08/01', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30036358', 'cinonan@mail.com', 'CARLOS',\n" +
            "        'INOÑAN', 'CHAPOÑAN', '20050520', 1, '2017/08/07', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30036722', 'jvega@mail.com', 'JHOSSY WHELLS',\n" +
            "        'VEGA', 'DOMINGUEZ', '20050604', 1, '2017/08/21', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30040213', 'ccisneros@mail.com', 'CESAR POLANKO',\n" +
            "        'CISNEROS', 'BAUTISTA', '20050533', 1, '2018/07/09', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30040412', 'mayala@mail.com', 'MARISOL MARISA',\n" +
            "        'AYALA', 'ARMES', '20056658', 1, '2018/08/06', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010215', '30046817', 'jlopez@mail.com', 'JOSE ANTONIO',\n" +
            "        'LOPEZ', 'GARCIA', '20061800', 1, '2021/01/18', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30035800', 'denciso@mail.com', 'DESIDERIO',\n" +
            "        'ENCISO', 'VILLALOBOS', '20053237', 1, '2017/05/12', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30040424', 'jortiz@mail.com', 'JULISSA',\n" +
            "        'ORTIZ', 'BARBARAN', '20056653', 1, '2018/08/06', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032020', 'elozada@mail.com', 'ELQUI ANTONIO',\n" +
            "        'LOZADA', 'SALAS', '20048579', 1, '2016/05/14', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032481', 'lcersuolo@mail.com', 'LUIS ALBERTO',\n" +
            "        'CERASUOLO', 'CAVERO', '20050451', 1, '2016/07/20', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name,\n" +
            "                         post_code, post_type_id, start_date, superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30032708', 'jlarico@mail.com', 'JAIME DANIEL',\n" +
            "        'LARICO', 'QUISPE', '20050611', 1, '2016/08/15', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('12', '1210', '1212', '10011454', 2, '10011454', '121010209', '30036361', 'lfachin@mail.com', 'LUIS IZAEL',\n" +
            "        'FACHIN', 'AHUANARI', '20047513', 1, '2017/08/07', '30030712');\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('10', '1025', '1011', '10011454', 3, '10001293', '101124010', '30001416', 'llopez@mail.com', 'LUIS MIGUEL',\n" +
            "        'LÓPEZ', 'GÓMEZ', '20029073', 19, '2011/08/23', null);\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('10', '1025', '1012', '10011454', 1, '10012764', '101124012', '30009554', 'calvarez@mail.com', 'CARLOS',\n" +
            "        'ÁLVAREZ', 'PÉREZ', '20000197', 20, '2010/07/01', null);\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('10', '1025', '1011', '10011454', 1, '10012764', '101120101', '30025842', 'phuaman@mail.com', 'PRESIDENCIO',\n" +
            "        'HUAMÁN', 'PATRICIO', '20000000', 21, '2014/03/08', null);\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('10', '1025', '1011', '10011454', 4, '10000008', '101122001', '30041624', 'jvasquez@mail.com', 'JORGE LUIS',\n" +
            "        'VÁSQUEZ', 'CHÁVEZ', '20000011', 22, '2019/01/28', null);\n" +
            "insert into employee_sap(society_code, division_code, physical_location_code, management_level_2_code, management_id,\n" +
            "                         organizational_unit_code, cost_center_code, employee_code, employee_email, employee_first_name,\n" +
            "                         employee_father_family_name, employee_mother_family_name, post_code, post_type_id, start_date,\n" +
            "                         superior_employee_code)\n" +
            "values ('10', '1025', '1011', '10011454', 4, '10000009', '101122005', '30000218', 'cgarcia@mail.com', 'CESAR',\n" +
            "        'GARCÍA', 'LUPANA', '20005292', 22, '2017/09/01', '30041624');\n" +
            "\n" +
            "-- User --\n" +
            "insert into \"user\"(employee_code, role)\n" +
            "values ('30036361', 0);\n" +
            "insert into \"user\"(employee_code, role)\n" +
            "values ('30030712', 0);\n" +
            "insert into \"user\"(employee_code, role)\n" +
            "values ('30030673', 0);\n" +
            "insert into \"user\"(employee_code, role)\n" +
            "values ('30044827', 1);\n" +
            "insert into \"user\"(employee_code, role)\n" +
            "values ('30036358', 7);\n" +
            "insert into \"user\"(employee_code, role)\n" +
            "values ('30046325', 2);\n" +
            "insert into \"user\"(employee_code, role)\n" +
            "values ('30001416', 4);\n" +
            "insert into \"user\"(employee_code, role)\n" +
            "values ('30009554', 5);\n" +
            "insert into \"user\"(employee_code, role)\n" +
            "values ('30025842', 6);\n";

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            return new APIGatewayProxyResponseEvent().withStatusCode(200)
                    .withBody("{\"message\": \"done\"}").withHeaders(DEFAULT_HEADERS);
        } catch (SQLException e) {
            log.debug("Error initializing database", e);
            throw new RuntimeException(e);
        } finally {
            if (statement != null)
                close(statement);
        }
    }
}
