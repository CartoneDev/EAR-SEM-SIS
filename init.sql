
CREATE USER earer WITH PASSWORD 'Badmin42' ;
ALTER USER earer WITH SUPERUSER;

CREATE DATABASE sis
    WITH
    OWNER = earer;

\c sis;

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: attendance; Type: TABLE; Schema: public; Owner: earer
--

CREATE TABLE public.attendance (
                                   course_id integer NOT NULL,
                                   id integer NOT NULL,
                                   program_id integer,
                                   attendance_type character varying(255) NOT NULL,
                                   CONSTRAINT attendance_attendance_type_check CHECK (((attendance_type)::text = ANY ((ARRAY['MANDATORY'::character varying, 'VOLUNTARY'::character varying, 'VOLUNTARY_FORCED'::character varying])::text[])))
);


ALTER TABLE public.attendance OWNER TO earer;

--
-- Name: attendance_seq; Type: SEQUENCE; Schema: public; Owner: earer
--

CREATE SEQUENCE public.attendance_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.attendance_seq OWNER TO earer;

--
-- Name: course; Type: TABLE; Schema: public; Owner: earer
--

CREATE TABLE public.course (
                               credits integer NOT NULL,
                               guarantor_id integer,
                               hours_lecture integer NOT NULL,
                               hours_practise integer NOT NULL,
                               id integer NOT NULL,
                               description character varying(255) NOT NULL,
                               name character varying(255) NOT NULL
);


ALTER TABLE public.course OWNER TO earer;

--
-- Name: course_seq; Type: SEQUENCE; Schema: public; Owner: earer
--

CREATE SEQUENCE public.course_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.course_seq OWNER TO earer;

--
-- Name: course_teacher; Type: TABLE; Schema: public; Owner: earer
--

CREATE TABLE public.course_teacher (
                                       course_id integer NOT NULL,
                                       teacher_id integer NOT NULL
);


ALTER TABLE public.course_teacher OWNER TO earer;

--
-- Name: enrollment_record; Type: TABLE; Schema: public; Owner: earer
--

CREATE TABLE public.enrollment_record (
                                          course_id integer NOT NULL,
                                          id integer NOT NULL,
                                          student_id integer NOT NULL,
                                          grade character varying(255),
                                          sem_year character varying(255) NOT NULL,
                                          CONSTRAINT enrollment_record_grade_check CHECK (((grade)::text = ANY ((ARRAY['UNGRADED'::character varying, 'A'::character varying, 'B'::character varying, 'C'::character varying, 'D'::character varying, 'E'::character varying, 'F'::character varying])::text[])))
);


ALTER TABLE public.enrollment_record OWNER TO earer;

--
-- Name: enrollment_record_seq; Type: SEQUENCE; Schema: public; Owner: earer
--

CREATE SEQUENCE public.enrollment_record_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.enrollment_record_seq OWNER TO earer;

--
-- Name: prerequisite; Type: TABLE; Schema: public; Owner: earer
--

CREATE TABLE public.prerequisite (
                                     id integer NOT NULL,
                                     is_mandatory boolean NOT NULL,
                                     prerequisite_course_id integer NOT NULL,
                                     requested_course_id integer NOT NULL
);


ALTER TABLE public.prerequisite OWNER TO earer;

--
-- Name: prerequisite_seq; Type: SEQUENCE; Schema: public; Owner: earer
--

CREATE SEQUENCE public.prerequisite_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.prerequisite_seq OWNER TO earer;

--
-- Name: program; Type: TABLE; Schema: public; Owner: earer
--

CREATE TABLE public.program (
                                id integer NOT NULL,
                                description character varying(255) NOT NULL,
                                name character varying(255) NOT NULL
);


ALTER TABLE public.program OWNER TO earer;

--
-- Name: program_seq; Type: SEQUENCE; Schema: public; Owner: earer
--

CREATE SEQUENCE public.program_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.program_seq OWNER TO earer;

--
-- Name: room; Type: TABLE; Schema: public; Owner: earer
--

CREATE TABLE public.room (
                             capacity integer NOT NULL,
                             id integer NOT NULL,
                             identifier character varying(255) NOT NULL,
                             type character varying(255) NOT NULL,
                             CONSTRAINT room_type_check CHECK (((type)::text = ANY ((ARRAY['AUDITORIUM'::character varying, 'CLASSROOM'::character varying, 'PC_ROOM'::character varying])::text[])))
);


ALTER TABLE public.room OWNER TO earer;

--
-- Name: room_seq; Type: SEQUENCE; Schema: public; Owner: earer
--

CREATE SEQUENCE public.room_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.room_seq OWNER TO earer;

--
-- Name: schedule; Type: TABLE; Schema: public; Owner: earer
--

CREATE TABLE public.schedule (
                                 capacity integer NOT NULL,
                                 course_id integer NOT NULL,
                                 id integer NOT NULL,
                                 room_id integer NOT NULL,
                                 teacher_id integer NOT NULL,
                                 date_from timestamp without time zone NOT NULL,
                                 date_to timestamp without time zone NOT NULL,
                                 lecture_type character varying(255) NOT NULL,
                                 CONSTRAINT schedule_lecture_type_check CHECK (((lecture_type)::text = ANY ((ARRAY['LECTURE'::character varying, 'PRACTISE'::character varying, 'LABORATORY'::character varying])::text[])))
);


ALTER TABLE public.schedule OWNER TO earer;

--
-- Name: schedule_seq; Type: SEQUENCE; Schema: public; Owner: earer
--

CREATE SEQUENCE public.schedule_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.schedule_seq OWNER TO earer;

--
-- Name: sis_user; Type: TABLE; Schema: public; Owner: earer
--

CREATE TABLE public.sis_user (
                                 id integer NOT NULL,
                                 program_id integer DEFAULT NUll,
                                 user_type character varying(31) NOT NULL,
                                 email character varying(255) NOT NULL,
                                 firstname character varying(255) NOT NULL,
                                 lastname character varying(255) NOT NULL,
                                 password character varying(255) NOT NULL,
                                 student_number character varying(255),
                                 username character varying(255) NOT NULL
);


ALTER TABLE public.sis_user OWNER TO earer;

--
-- Name: sis_user_course; Type: TABLE; Schema: public; Owner: earer
--

CREATE TABLE public.sis_user_course (
                                        course_id integer NOT NULL,
                                        guarantor_id integer NOT NULL
);


ALTER TABLE public.sis_user_course OWNER TO earer;

--
-- Name: sis_user_seq; Type: SEQUENCE; Schema: public; Owner: earer
--

CREATE SEQUENCE public.sis_user_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sis_user_seq OWNER TO earer;

--
-- Name: student_schedule; Type: TABLE; Schema: public; Owner: earer
--

CREATE TABLE public.student_schedule (
                                         schedule_id integer NOT NULL,
                                         student_id integer NOT NULL
);


ALTER TABLE public.student_schedule OWNER TO earer;

--
-- Name: attendance attendance_pkey; Type: CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT attendance_pkey PRIMARY KEY (id);


--
-- Name: course course_guarantor_id_key; Type: CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_guarantor_id_key UNIQUE (guarantor_id);


--
-- Name: course course_pkey; Type: CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (id);


--
-- Name: enrollment_record enrollment_record_pkey; Type: CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.enrollment_record
    ADD CONSTRAINT enrollment_record_pkey PRIMARY KEY (id);


--
-- Name: prerequisite prerequisite_pkey; Type: CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.prerequisite
    ADD CONSTRAINT prerequisite_pkey PRIMARY KEY (id);


--
-- Name: prerequisite prerequisite_requested_course_id_key; Type: CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.prerequisite
    ADD CONSTRAINT prerequisite_requested_course_id_key UNIQUE (requested_course_id);


--
-- Name: program program_pkey; Type: CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.program
    ADD CONSTRAINT program_pkey PRIMARY KEY (id);


--
-- Name: room room_identifier_key; Type: CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_identifier_key UNIQUE (identifier);


--
-- Name: room room_pkey; Type: CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_pkey PRIMARY KEY (id);


--
-- Name: schedule schedule_pkey; Type: CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.schedule
    ADD CONSTRAINT schedule_pkey PRIMARY KEY (id);


--
-- Name: sis_user_course sis_user_course_course_id_key; Type: CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.sis_user_course
    ADD CONSTRAINT sis_user_course_course_id_key UNIQUE (course_id);


--
-- Name: sis_user sis_user_pkey; Type: CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.sis_user
    ADD CONSTRAINT sis_user_pkey PRIMARY KEY (id);


--
-- Name: sis_user sis_user_username_key; Type: CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.sis_user
    ADD CONSTRAINT sis_user_username_key UNIQUE (username);


--
-- Name: course fk1es4ssqvebsxvsokttmgunj1c; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT fk1es4ssqvebsxvsokttmgunj1c FOREIGN KEY (guarantor_id) REFERENCES public.sis_user(id);


--
-- Name: schedule fk1psrumo7fgkd16p438etda0i6; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.schedule
    ADD CONSTRAINT fk1psrumo7fgkd16p438etda0i6 FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: schedule fk525fj3voegh7ewd45q766hvyi; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.schedule
    ADD CONSTRAINT fk525fj3voegh7ewd45q766hvyi FOREIGN KEY (teacher_id) REFERENCES public.sis_user(id);


--
-- Name: prerequisite fk9px9rj9o9329ccyfute1246jo; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.prerequisite
    ADD CONSTRAINT fk9px9rj9o9329ccyfute1246jo FOREIGN KEY (requested_course_id) REFERENCES public.course(id);


--
-- Name: sis_user_course fkbbpsab7qqxjj6s3h6vxi0662t; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.sis_user_course
    ADD CONSTRAINT fkbbpsab7qqxjj6s3h6vxi0662t FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: attendance fke7ib3fys16x0fwe8siwat310f; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT fke7ib3fys16x0fwe8siwat310f FOREIGN KEY (program_id) REFERENCES public.program(id);


--
-- Name: student_schedule fkflbrh0ee3ua9ubbpa4qlvwxxg; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.student_schedule
    ADD CONSTRAINT fkflbrh0ee3ua9ubbpa4qlvwxxg FOREIGN KEY (schedule_id) REFERENCES public.schedule(id);


--
-- Name: schedule fkh2hdhbss2x31ns719hka6enma; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.schedule
    ADD CONSTRAINT fkh2hdhbss2x31ns719hka6enma FOREIGN KEY (room_id) REFERENCES public.room(id);


--
-- Name: enrollment_record fkh96ytsitl8xt99oq51wpxdhvj; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.enrollment_record
    ADD CONSTRAINT fkh96ytsitl8xt99oq51wpxdhvj FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: prerequisite fkhisx6tiyve5566sm51wlm96us; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.prerequisite
    ADD CONSTRAINT fkhisx6tiyve5566sm51wlm96us FOREIGN KEY (prerequisite_course_id) REFERENCES public.course(id);


--
-- Name: student_schedule fkhkff66qqley1st3gotn8ddrlb; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.student_schedule
    ADD CONSTRAINT fkhkff66qqley1st3gotn8ddrlb FOREIGN KEY (student_id) REFERENCES public.sis_user(id);


--
-- Name: sis_user_course fkhnf48lx64fcapa08gaken56c4; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.sis_user_course
    ADD CONSTRAINT fkhnf48lx64fcapa08gaken56c4 FOREIGN KEY (guarantor_id) REFERENCES public.sis_user(id);


--
-- Name: enrollment_record fkiv5vbawlo3tdmq87krjrippbw; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.enrollment_record
    ADD CONSTRAINT fkiv5vbawlo3tdmq87krjrippbw FOREIGN KEY (student_id) REFERENCES public.sis_user(id);


--
-- Name: attendance fkmwxjtpjcf6y7m8x2jqeryj255; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT fkmwxjtpjcf6y7m8x2jqeryj255 FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: sis_user fkn9mkmevorx0huo7uoqbch41lo; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.sis_user
    ADD CONSTRAINT fkn9mkmevorx0huo7uoqbch41lo FOREIGN KEY (program_id) REFERENCES public.program(id);


--
-- Name: course_teacher fkrna6ik293g84mo3rslnkk7m1a; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.course_teacher
    ADD CONSTRAINT fkrna6ik293g84mo3rslnkk7m1a FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: course_teacher fktrvs4nbc3d6hedsowvanmnq1w; Type: FK CONSTRAINT; Schema: public; Owner: earer
--

ALTER TABLE ONLY public.course_teacher
    ADD CONSTRAINT fktrvs4nbc3d6hedsowvanmnq1w FOREIGN KEY (teacher_id) REFERENCES public.sis_user(id);


--
-- PostgreSQL database dump complete
--





