/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.cmf.dam.contentstores.rawstoredrivers;

import rife.cmf.dam.ContentDataUser;
import rife.cmf.dam.ContentDataUserWithoutResult;
import rife.cmf.dam.exceptions.ContentManagerException;
import rife.database.Datasource;
import rife.database.queries.Select;
import rife.engine.Context;

public class org_postgresql_Driver extends generic {
    public org_postgresql_Driver(Datasource datasource) {
        super(datasource);
    }

    @Override
    protected void _useContentData(final Select retrieveContentChunks, final int id, final ContentDataUserWithoutResult user)
    throws ContentManagerException {
        inTransaction(() -> super._useContentData(retrieveContentChunks, id, user));
    }

    @Override
    protected <ResultType> ResultType _useContentDataResult(final Select retrieveContentChunks, final int id, final ContentDataUser<ResultType> user)
    throws ContentManagerException {
        return inTransaction(() -> super._useContentDataResult(retrieveContentChunks, id, user));
    }

    @Override
    protected void _serveContentData(final Select retrieveContentChunks, final Context context, final int id)
    throws ContentManagerException {
        inTransaction(() -> super._serveContentData(retrieveContentChunks, context, id));
    }
}
