package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.Region;
import com.nmtrails.appcontest.entities.RegionView;
import com.nmtrails.appcontest.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service
public class RegionServiceImpl implements RegionService {

    RegionRepository repo;

    @Autowired
    public RegionServiceImpl(RegionRepository repo) {
        this.repo = repo;
    }

    /**
     * Return region given a region id
     * */
    @Override
    public Region findById(Long id) {
        Optional<Region> region = repo.findById(id);
        if (region.isPresent()) return region.get();
        else throw new RuntimeException("Region not found.");
    }

    /**
     * Return all region entries given {@link Pageable} object
     * */
    @Override
    public List<RegionView> findAll(Pageable pageable) {
        return repo.findAllBy(pageable).toList();
    }

    /**
     * Using a text file containing region names, generate and return random region. In case of error,
     * query first region entry from dB and return.
     * */
    @Override
    public RegionView randomRegion() {

        try {

            Scanner scanner = new Scanner(new File("componentdata/featuredregions.txt"));
            List<String> regionNames = new ArrayList<>();
            while (scanner.hasNextLine()) {
                regionNames.add(scanner.nextLine());
            }

            String random = regionNames.get(new Random().nextInt(regionNames.size()));
            return repo.findByName(random);

        } catch (FileNotFoundException e) {
        }

        PageRequest pr = PageRequest.of(1, 1);
        return repo.findAllBy(pr).toList().get(0);
    }
}
