INSERT INTO categorias (nombre) VALUES
                                    ('vientos fuertes'),
                                    ('inundaciones'),
                                    ('granizo'),
                                    ('nevadas'),
                                    ('calor extremo'),
                                    ('sequía'),
                                    ('derrumbes'),
                                    ('actividad volcánica'),
                                    ('incendios'),
                                    ('contaminación'),
                                    ('evento sanitario'),
                                    ('derrame'),
                                    ('intoxicación masiva');


INSERT INTO sinonimos (palabra, categoria_id) VALUES
                                                  ('viento',        (SELECT id FROM categorias WHERE nombre='vientos fuertes')),
                                                  ('temporal',      (SELECT id FROM categorias WHERE nombre='vientos fuertes')),
                                                  ('tormenta',      (SELECT id FROM categorias WHERE nombre='vientos fuertes')),
                                                  ('ráfaga',        (SELECT id FROM categorias WHERE nombre='vientos fuertes')),
                                                  ('vendaval',      (SELECT id FROM categorias WHERE nombre='vientos fuertes')),

                                                  ('inundación',    (SELECT id FROM categorias WHERE nombre='inundaciones')),
                                                  ('anegamiento',   (SELECT id FROM categorias WHERE nombre='inundaciones')),
                                                  ('crecida',       (SELECT id FROM categorias WHERE nombre='inundaciones')),
                                                  ('desborde',      (SELECT id FROM categorias WHERE nombre='inundaciones')),
                                                  ('lluvia',        (SELECT id FROM categorias WHERE nombre='inundaciones')),

                                                  ('granizo',       (SELECT id FROM categorias WHERE nombre='granizo')),
                                                  ('piedra',        (SELECT id FROM categorias WHERE nombre='granizo')),

                                                  ('nieve',         (SELECT id FROM categorias WHERE nombre='nevadas')),
                                                  ('nevada',        (SELECT id FROM categorias WHERE nombre='nevadas')),

                                                  ('calor',         (SELECT id FROM categorias WHERE nombre='calor extremo')),
                                                  ('ola de calor',  (SELECT id FROM categorias WHERE nombre='calor extremo')),
                                                  ('temperatura alta',(SELECT id FROM categorias WHERE nombre='calor extremo')),
                                                  ('térmico',       (SELECT id FROM categorias WHERE nombre='calor extremo')),

                                                  ('sequía',        (SELECT id FROM categorias WHERE nombre='sequía')),
                                                  ('falta de agua', (SELECT id FROM categorias WHERE nombre='sequía')),
                                                  ('escasez hídrica',(SELECT id FROM categorias WHERE nombre='sequía')),
                                                  ('árido',         (SELECT id FROM categorias WHERE nombre='sequía')),

                                                  ('derrumbe',      (SELECT id FROM categorias WHERE nombre='derrumbes')),
                                                  ('deslizamiento', (SELECT id FROM categorias WHERE nombre='derrumbes')),
                                                  ('alud',          (SELECT id FROM categorias WHERE nombre='derrumbes')),
                                                  ('corte de ruta', (SELECT id FROM categorias WHERE nombre='derrumbes')),

                                                  ('volcán',        (SELECT id FROM categorias WHERE nombre='actividad volcánica')),
                                                  ('erupción',      (SELECT id FROM categorias WHERE nombre='actividad volcánica')),

                                                  ('incendio',      (SELECT id FROM categorias WHERE nombre='incendios')),
                                                  ('fuego',         (SELECT id FROM categorias WHERE nombre='incendios')),
                                                  ('quema',         (SELECT id FROM categorias WHERE nombre='incendios')),
                                                  ('forestal',      (SELECT id FROM categorias WHERE nombre='incendios')),

                                                  ('contaminación', (SELECT id FROM categorias WHERE nombre='contaminación')),
                                                  ('vertido',       (SELECT id FROM categorias WHERE nombre='contaminación')),
                                                  ('basura',        (SELECT id FROM categorias WHERE nombre='contaminación')),
                                                  ('residuos',      (SELECT id FROM categorias WHERE nombre='contaminación')),

                                                  ('enfermedad',    (SELECT id FROM categorias WHERE nombre='evento sanitario')),
                                                  ('brote',         (SELECT id FROM categorias WHERE nombre='evento sanitario')),
                                                  ('epidemia',      (SELECT id FROM categorias WHERE nombre='evento sanitario')),
                                                  ('pandemia',      (SELECT id FROM categorias WHERE nombre='evento sanitario')),
                                                  ('virus',         (SELECT id FROM categorias WHERE nombre='evento sanitario')),

                                                  ('derrame',       (SELECT id FROM categorias WHERE nombre='derrame')),
                                                  ('petróleo',      (SELECT id FROM categorias WHERE nombre='derrame')),
                                                  ('químico',       (SELECT id FROM categorias WHERE nombre='derrame')),
                                                  ('aceite',        (SELECT id FROM categorias WHERE nombre='derrame')),

                                                  ('intoxicación',  (SELECT id FROM categorias WHERE nombre='intoxicación masiva')),
                                                  ('alimento',      (SELECT id FROM categorias WHERE nombre='intoxicación masiva')),
                                                  ('veneno',        (SELECT id FROM categorias WHERE nombre='intoxicación masiva'));
