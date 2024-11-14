import React, { useEffect, useRef, useState } from "react";
import * as d3 from "d3";

const TrellisGraph = ({ nodes, links }) => {
  const svgRef = useRef();
  const [dimensions, setDimensions] = useState({ width: 0, height: 0 });

  useEffect(() => {
    // Function which updates dimensions depending on client
    const updateDimensions = () => {
      const width = svgRef.current.parentNode.clientWidth;
      const height = svgRef.current.parentNode.clientHeight;
      setDimensions({ width, height });
    };
    // Sets up initial dimensions and even listener
    updateDimensions();
    window.addEventListener("resize", updateDimensions);

    // Cleanup
    return () => window.removeEventListener("resize", updateDimensions);
  }, []);

  useEffect(() => {
    const { width, height } = dimensions;

    // Edge case if there are no dimension available
    if (width === 0 || height === 0) return;

    const svg = d3.select(svgRef.current);
    svg.attr("width", width).attr("height", height);

    // This clears previous render
    svg.selectAll("*").remove();

    // Creates array of states from nodes array
    const uniqueStates = [...new Set(nodes.map((node) => node.state))];

    // Define node positioning and scaling
    const xScale = d3
      .scalePoint()
      .domain(nodes.map((node) => node.time))
      .range([20, width - 20]);

    const yScale = d3
      .scalePoint()
      .domain(uniqueStates)
      .range([20, height - 20]);

    // Draw links
    svg
      .selectAll("line")
      .data(links)
      .join("line")
      .attr("x1", (d) => xScale(d.source.time))
      .attr("y1", (d) => yScale(d.source.state))
      .attr("x2", (d) => xScale(d.target.time))
      .attr("y2", (d) => yScale(d.target.state))
      .attr("stroke", "black")
      .attr("stroke-width", 1);

    // Draw Nodes
    svg
      .selectAll("circle")
      .data(nodes)
      .join("circle")
      .attr("cx", (d) => xScale(d.time))
      .attr("cy", (d) => yScale(d.state))
      .attr("r", 5)
      .attr("fill", "blue");

    // Label nodes
    svg
      .selectAll("text")
      .data(nodes)
      .join("text")
      .attr("x", (d) => xScale(d.time))
      .attr("y", (d) => yScale(d.state) - 10)
      .attr("text-anchor", "middle")
      .attr("font-size", "10px")
      .text((d) => d.state);
  }, [nodes, links, dimensions]);

  return <svg ref={svgRef}></svg>;
};

export default TrellisGraph;
