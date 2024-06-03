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
package org.qi4j.library.crudui;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.polygene.api.association.ManyAssociation;
import org.apache.polygene.api.entity.EntityReference;

/**
 * If you want to catch calls to ManyAssociations, then create a GenericConcern
 * that wraps the Polygene-supplied ManyAssociation instance with ManyAssociationWrappers. Override
 * methods to perform your custom code.
 */
public class ManyAssociationWrapper<T>
    implements ManyAssociation<T>
{
    protected ManyAssociation<T> next;

    public ManyAssociationWrapper( ManyAssociation<T> next )
    {
        this.next = next;
    }

    public ManyAssociation<T> next()
    {
        return next;
    }

    @Override
    public int count()
    {
        return next.count();
    }

    @Override
    public boolean contains( T entity )
    {
        return next.contains( entity );
    }

    @Override
    public boolean add( int i, T entity )
    {
        return next.add( i, entity );
    }

    @Override
    public boolean add( T entity )
    {
        return next.add( entity );
    }

    @Override
    public boolean remove( T entity )
    {
        return next.remove( entity );
    }

    @Override
    public boolean clear()
    {
        return next.clear();
    }

    @Override
    public T get( int i )
    {
        return next.get( i );
    }

    @Override
    public List<T> toList()
    {
        return next.toList();
    }

    @Override
    public Set<T> toSet()
    {
        return next.toSet();
    }

    @Override
    public Stream<EntityReference> references()
    {
        return next.references();
    }

    @Override
    public Iterator<T> iterator()
    {
        return next.iterator();
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
