/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package org.qi4j.library.javafx.support;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.polygene.api.association.NamedAssociation;
import org.apache.polygene.api.entity.EntityReference;

/**
 * If you want to catch calls to NamedAssociations, then create a GenericConcern
 * that wraps the Polygene-supplied NamedAssociations instance with NamedAssociationsWrapper. Override
 * methods to perform your custom code.
 */
public class NamedAssociationWrapper<T>
    implements NamedAssociation<T>
{
    protected NamedAssociation<T> next;

    public NamedAssociationWrapper( NamedAssociation<T> next )
    {
        this.next = next;
    }

    public NamedAssociation<T> next()
    {
        return next;
    }

    @Override
    public Iterator<String> iterator()
    {
        return next.iterator();
    }

    @Override
    public int count()
    {
        return next.count();
    }

    @Override
    public boolean containsName( String name )
    {
        return next.containsName( name );
    }

    @Override
    public boolean put( String name, T entity )
    {
        return next.put( name, entity );
    }

    @Override
    public boolean remove( String name )
    {
        return next.remove( name );
    }

    @Override
    public boolean clear()
    {
        return next.clear();
    }

    @Override
    public T get( String name )
    {
        return next.get( name );
    }

    @Override
    public String nameOf( T entity )
    {
        return next.nameOf( entity );
    }

    @Override
    public Map<String, T> toMap()
    {
        return next.toMap();
    }

    @Override
    public Stream<Map.Entry<String, EntityReference>> references()
    {
        return next.references();
    }

    @Override
    public EntityReference referenceOf( String name )
    {
        return next.referenceOf( name );
    }

    @Override
    public int hashCode()
    {
        return next.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals( Object obj )
    {
        return next.equals( obj );
    }

    @Override
    public String toString()
    {
        return next.toString();
    }
}
