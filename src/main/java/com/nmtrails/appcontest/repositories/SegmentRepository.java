package com.nmtrails.appcontest.repositories;

import com.nmtrails.appcontest.entities.Segment;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long> {

    /**
     * Returns a bounding rectangle (an envelope) for all segments in the specified
     * trails
     *
     * @param ids ids of the trails
     * @returns the envelope bounding the trails
     */
    @Query(value = "SELECT ST_AsText(ST_Extent(segments.track)) " +
                   "FROM segments WHERE trail_trail_id IN :ids",
           nativeQuery = true)
    String findTrailsExtent(List<Long> ids);
}
