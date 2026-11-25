-- Los mismos 3 cultivos ficticios de DatosEjemplo.kt de la app (kg/ha).
-- VALORES DE DEMOSTRACION: no son recomendaciones agronomicas.
-- MERGE ... KEY(nombre): se puede ejecutar en cada arranque sin duplicar filas.
MERGE INTO cultivos (nombre, rendimiento_base, n, p2o5, k2o, cao, mgo) KEY (nombre)
    VALUES ('Palto', 10.0, 60.0, 20.0, 80.0, 30.0, 15.0);
MERGE INTO cultivos (nombre, rendimiento_base, n, p2o5, k2o, cao, mgo) KEY (nombre)
    VALUES ('Cítricos', 40.0, 120.0, 40.0, 150.0, 80.0, 30.0);
MERGE INTO cultivos (nombre, rendimiento_base, n, p2o5, k2o, cao, mgo) KEY (nombre)
    VALUES ('Tomate', 80.0, 200.0, 80.0, 300.0, 150.0, 40.0);
