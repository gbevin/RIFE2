/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.authentication.sessionmanagers.databasedrivers;

import rife.database.Datasource;

public class com_mysql_cj_jdbc_Driver extends generic {
    public com_mysql_cj_jdbc_Driver(Datasource datasource) {
        super(datasource);

        removeAuthenticationSessStartIndex_ = "DROP INDEX " + createAuthentication_.getTable() + "_IDX ON " + createAuthentication_.getTable();
    }
}
