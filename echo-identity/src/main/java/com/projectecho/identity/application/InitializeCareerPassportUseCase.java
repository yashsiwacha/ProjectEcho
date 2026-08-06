package com.projectecho.identity.application;

import com.projectecho.identity.domain.EmailAddress;
import com.projectecho.identity.domain.JobTitle;
import com.projectecho.identity.domain.Name;
import com.projectecho.shared.domain.PassportId;

public interface InitializeCareerPassportUseCase {
    PassportId initialize(Name name, EmailAddress email, JobTitle jobTitle);
}
