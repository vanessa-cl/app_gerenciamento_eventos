package util.database

object DatabaseInit {
    fun init() {
        val connection = DatabaseUtil.getConnection()
        val statement = connection?.createStatement()
        val sqlStatements = listOf(
            """
            CREATE TABLE IF NOT EXISTS eventos (
                id INTEGER PRIMARY KEY,
                nome TEXT,
                descricao TEXT,
                valorInscricao REAL,
                dataInicio TEXT,
                dataFim TEXT,
                status TEXT,
                turno TEXT
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS palestras (
                id INTEGER PRIMARY KEY,
                idEvento INTEGER,
                titulo TEXT,
                palestrante TEXT,
                limiteParticipantes INTEGER,
                local TEXT,
                data TEXT,
                horarioInicio TEXT,
                horarioFim TEXT,
                status TEXT,
                FOREIGN KEY (idEvento) REFERENCES eventos(id)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS participantes (
                id INTEGER PRIMARY KEY,
                nome TEXT,
                email TEXT,
                cpf TEXT,
                cargo TEXT
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS palestra_participantes (
                idPalestra INTEGER,
                idParticipante INTEGER,
                PRIMARY KEY (idPalestra, idParticipante),
                FOREIGN KEY (idPalestra) REFERENCES palestras(id),
                FOREIGN KEY (idParticipante) REFERENCES participantes(id)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS fila_espera (
                idPalestra INTEGER,
                idParticipante INTEGER,
                PRIMARY KEY (idPalestra, idParticipante),
                FOREIGN KEY (idPalestra) REFERENCES palestras(id),
                FOREIGN KEY (idParticipante) REFERENCES participantes(id)
            );
            """
        )

        sqlStatements.forEach { sql ->
            statement?.execute(sql)
        }
    }
}