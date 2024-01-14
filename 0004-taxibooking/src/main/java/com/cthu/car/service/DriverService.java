package com.cthu.car.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cthu.car.model.dto.DriverInfoDto;
import com.cthu.car.model.entity.Car;
import com.cthu.car.model.entity.Drivers;
import com.cthu.car.model.form.DriverForm;
import com.cthu.car.model.repo.CarNameRepo;
import com.cthu.car.model.repo.CarRepo;
import com.cthu.car.model.repo.DriversRepo;
import com.cthu.car.model.repo.TownshipRepo;
import com.cthu.car.utils.exceptions.ApiBusinessException;

@Service
public class DriverService {

	@Autowired
	private DriversRepo driverRepo;
	
	@Autowired
	private TownshipRepo townshipRepo;
	
	@Autowired
	private CarRepo carRepo;
	
	@Autowired
	private CarNameRepo carNameRepo;

	public DriverInfoDto create(DriverForm form) {
		
		Drivers driver = driverRepo.save(form.driverEntity(id -> townshipRepo.findById(id)
				.orElseThrow(() -> new ApiBusinessException("Invalid Township"))));
		
		Car car = carRepo.save(form.carEntity(
					driver.getLoginId(),
					id -> driverRepo.findById(id)
					.orElseThrow(() -> new ApiBusinessException("Invalid Driver Id")), 
					id -> carNameRepo.findById(id)
					.orElseThrow(() -> new ApiBusinessException("Invalid Township Id"))
					));
		return form.getDriverInfoDto(driver, car);
	}
}