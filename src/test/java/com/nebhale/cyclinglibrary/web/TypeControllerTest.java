
package com.nebhale.cyclinglibrary.web;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;

import com.nebhale.cyclinglibrary.model.Type;
import com.nebhale.cyclinglibrary.repository.TypeRepository;

public final class TypeControllerTest {

    private final TypeRepository typeRepository = mock(TypeRepository.class);

    private final TypeController controller = new TypeController(typeRepository);

    @Test
    public void find() {
        Set<Type> types = Collections.emptySet();
        when(this.typeRepository.find()).thenReturn(types);

        assertSame(types, this.controller.find());
    }

    @Test
    public void create() {
        Type type = new Type(0, "test-name");
        when(this.typeRepository.create("test-name")).thenReturn(type);
        
        Type result = this.controller.create(new TypeInput("test-name"));
        
        assertSame(type, result);
    }
    
    @Test
    public void read() {
        Type type = new Type(0, "test-name");
        when(this.typeRepository.read(0)).thenReturn(type);
        
        Type result = this.controller.read(0);
        
        assertSame(type, result);
    }
    
    @Test
    public void update() {
        Type type = new Type(0, "new-test-name");
        when(this.typeRepository.update(0, "new-test-name")).thenReturn(type);
        
        Type result = this.controller.update(0, new TypeInput("new-test-name"));
        
        assertSame(type, result);
    }
    
    @Test
    public void delete() {
        this.controller.delete(0);
        
        verify(this.typeRepository).delete(0);
    }

}
