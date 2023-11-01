package hello.capstone.validation;

import hello.capstone.validation.group.NotBlanckGroup;
import hello.capstone.validation.group.PatternCheckGroup;
import jakarta.validation.GroupSequence;

@GroupSequence({NotBlanckGroup.class, PatternCheckGroup.class})
public interface ValidationSequence {

}
